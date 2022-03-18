#!/usr/bin/python3
# Copyright 2020 Uraniborg authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""Worker thread that automates various steps of observation process.

This is a script that automates the installation and launching of Hubble. It
also detects execution state and result and automates the 'downloading' of
Hubble data back to host.
"""
import argparse
import io   # to convert regular buffer to in-memory bytes buffer for tarfileobj
import json
import logging
import os
import shutil  # to help move files
import sys
import tarfile  # to untar decompressed adb backup file
import zlib  # to decompress adb backup

import syscall_wrapper

AdbWrapper = syscall_wrapper.AdbWrapper
SyscallWrapper = syscall_wrapper.SyscallWrapper

HUBBLE_PACKAGE_NAME = "com.uraniborg.hubble"
HUBBLE_LOGCAT_TAG = "HUBBLE"
HUBBLE_RESULT_STRING = "Results are available at:"


def parse_arguments():
  """Parses cmdline arguments.

  Returns:
    An object containing information about arguments that are passed within
    this namespace.
  """
  parser = argparse.ArgumentParser(
      description="A script that automates the installation, launch, and"
                  "result-pulling from Hubble. The user should only need to"
                  "supply the location of Hubble APK, and this script should"
                  "take care of the rest. In the case that more than 1 Android"
                  "devices is plugged in, user specification will be required.",
      formatter_class=argparse.ArgumentDefaultsHelpFormatter)

  parser.add_argument("-H", "--hubble", required=False,
                      default="../../prebuilts/APK/latest",
                      help="The path to where Hubble APK is on your "
                      "system/host.")
  parser.add_argument("-o", "--output", required=False,
                      default=os.path.join(os.getcwd(), "results"),
                      help="Specifies the output directory where the "
                      "\"results\" directory can be found.")
  parser.add_argument("-D", "--debug", required=False, action="count",
                      help="If specified, debugging mode is turned on.")
  parser.add_argument("--use-old-results-classification", required=False,
                      action="count",
                      help="If specified, results will be classified using "
                           "old classification using ADB convention.")
  args = parser.parse_args()
  return args


def set_up_logging(args):
  """Sets up various logging parameters.

  Args:
    args: Parsed arguments from calling argparse.ArgumentParser()

  Returns:
    A logger object suitable for usage.
  """
  logger = logging.getLogger(__name__)

  if args.debug:
    logger.setLevel(logging.DEBUG)
  else:
    logger.setLevel(logging.INFO)

  s_handler = logging.StreamHandler()
  s_format = logging.Formatter(
      "%(levelname)s:%(filename)s:%(funcName)s(%(lineno)d): %(message)s")
  s_handler.setFormatter(s_format)

  logger.addHandler(s_handler)
  return logger


def supported_platform(logger):
  """Checks if this script is running on supported platform.

  Args:
    logger: A valid logger instance to log debug/error messages.

  Returns:
    True if this platform is supported.
  """
  # TODO(billy): Look into supporting Windows in the near future.
  logger.debug("Current platform: {}".format(sys.platform))
  if not (sys.platform == "linux" or sys.platform == "darwin"):
    logger.error("Sorry, your OS is currently unsupported for this script.")
    return False

  if not (sys.version_info.major == 3 and sys.version_info.minor >= 5):
    logger.error("This script requires Python 3.5 or higher!")
    logger.error("You are using Python {}.{}.".format(sys.version_info.major,
                                                      sys.version_info.minor))
    return False
  return True


def verify_hubble(args, logger):
  # First, resolve the path if it's a symlink
  args.hubble = os.path.realpath(args.hubble)
  logger.debug("Hubble's realpath: %s", args.hubble)

  if not args.hubble.endswith(".apk"):
    logger.error("Hubble APK to be installed must have the .apk extension.")
    return False

  return True


def adb_installed(logger):
  cmd = ["which", "adb"]
  sw = SyscallWrapper(logger)
  sw.call_returnable_command(cmd)
  if sw.return_code == 0:
    logger.debug("ADB was found on system, installed at: %s", sw.result_final)
    return True

  logger.debug("ADB was NOT found on system!")
  return False


def clear_logcat(adb_wrapper):
  adb_wrapper.logcat_clear()


def is_hubble_installed(adb_wrapper, logger):
  """Checks if Hubble is already installed on device.

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    logger: A logger object to log debug or error messages.

  Returns:
    A boolean indicating whether or not Hubble was installed on the connected
    device.

  Raises:
    RuntimeError: if something went wrong within the ADB call. This means the
                  status of Hubble installation can't be determined, therefore
                  is not suitable as a return value.
  """
  check_installation_cmd = ["pm", "list", "packages"]
  if not adb_wrapper.shell(check_installation_cmd):
    raise RuntimeError("Querying for Hubble installation failed.")

  for package_name in adb_wrapper.get_result():
    if HUBBLE_PACKAGE_NAME in package_name:
      logger.debug("Hubble was previously installed")
      return True
  logger.debug("Hubble was not found installed.")
  return False


def is_xiaomi_phone(adb_wrapper, logger):
  """Checks if the connected device is a Xiami device.

  We do this by checking various strings within the device property for any
  signs of "xiaomi"

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    logger: A logger object to log debug or error messages.

  Returns:
    A boolean indicating whether or not the connected device is a Xiaomi device.
  """
  getprop_cmd = ["getprop"]
  if not adb_wrapper.shell(getprop_cmd):
    return False

  for line in adb_wrapper.get_result():
    components = line.split(": ")
    if "oem" in components[0].strip().lower():
      logger.debug("Found oem in field: %s", components[0])
      if "xiaomi" in components[1].strip().lower():
        logger.debug("Found xiaomi in value: %s", components[1])
        return True
    elif "brand" in components[0].strip().lower():
      logger.debug("Found brand in field: %s", components[0])
      if "xiaomi" in components[1].strip().lower():
        logger.debug("Found xiaomi in value %s", components[1])
        return True
  return False


def launch_xiaomi_file_explorer(adb_wrapper):
  return adb_wrapper.am_start(
      "com.mi.android.globalFileexplorer",
      "com.android.fileexplorer.FileExplorerTabActivity")


def adb_push_hubble(adb_wrapper, hubble_path):
  """Drops the Hubble APK onto device (used when direct installation fails).

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    hubble_path: The path to where Hubble resides on the host.

  """
  hubble_abs_path = os.path.abspath(hubble_path)
  # For some reasons, /storage/sdcard0 does not exist on all Xiaomi devices
  target_locations = [
      "/storage/sdcard0/Download/",
      "/sdcard/Download/"
  ]

  try_another = False
  for location in target_locations:
    if not adb_wrapper.push(hubble_abs_path, location):
      continue
    line = adb_wrapper.get_result()
    if line:
      if "error" in line or "fail" in line:
        try_another = True
    if not try_another:
      break


def install_hubble(adb_wrapper, args, logger):
  """Performs installation of Hubble via "adb install".

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    args: The arguments object used in the following way:
          .hubble: To be read from to determine hubble's path
          .error_message: To be written to in case of errors.
    logger: A logger object to log debug or error messages.

  Returns:
    A boolean indicating the success of the installation process.
  """
  hubble_abs_path = os.path.abspath(args.hubble)
  if not os.path.exists(hubble_abs_path):
    logger.error("{} does not exist!".format(args.hubble))
    return False

  if not os.path.isfile(hubble_abs_path):
    logger.error("{} is not a file".format(args.hubble))
    return False

  # do the actual installation by calling adb install
  if not adb_wrapper.install(hubble_abs_path):
    return False

  # sometimes, the return code states that installation is successful, but
  # in actuality there may still be some other failure(s).
  for l in adb_wrapper.get_result():
    logger.debug("installation result: {}".format(l))
    if "fail" in l.lower():
      args.error_message = l.strip()
      return False

  return True


def remove_previous_installation(adb_wrapper):
  return adb_wrapper.uninstall(HUBBLE_PACKAGE_NAME)


def launch_hubble(adb_wrapper):
  return adb_wrapper.am_start(action_name=None,
                              extra_string=None,
                              package_name=HUBBLE_PACKAGE_NAME,
                              component_name="{0}.MainActivity".format(
                                  HUBBLE_PACKAGE_NAME))


def terminate_logcat(buffer, logger):
  if HUBBLE_LOGCAT_TAG in buffer and HUBBLE_RESULT_STRING in buffer:
    logger.debug("Execution is done! Extracting result location...")
    comps = buffer.split()
    logger.debug("comps: {}".format(comps))
    results_dir = comps[-1]
    logger.debug("results dir: {}".format(results_dir))
    return results_dir
  return None


def wait_for_results(adb_wrapper, logger):
  logger.info("Waiting for results from Hubble execution...")
  patterns = ["HUBBLE:W", "*:S"]
  return adb_wrapper.logcat_find(patterns, terminate_logcat)


def classify_directory_using_adb_format(adb_wrapper,
                                        source,
                                        results_dir,
                                        device,
                                        logger):
  """Decides which directory in results/ to dump new result to.

  This method is grandfathered as it was the original way of classification,
  using ADB's information, on product name, model name, and device name.

  Args:
    adb_wrapper: An AdbWrapper instance.
    source: the source directory (on target device) containing new results.
    results_dir: the umbrella results/ directory.
    device: A DeviceInfo instance containing the device to operate on.
    logger: A logger object to log debug or error messages.

  Returns:
    A string representing the final directory (on host) where results are pulled
    to. <code>None</code> is returned if any failure is encountered along the
    way.
  """
  target_dir_parent = os.path.join(results_dir, "{}-{}-{}".format(
      device.product_name, device.model_name, device.device_name))
  if not os.path.exists(target_dir_parent):
    logger.debug("{} does not exist yet. Creating...".format(target_dir_parent))
    os.makedirs(target_dir_parent)

  target_dir = ""
  for i in range(1000):
    target_dir = os.path.join(target_dir_parent, "{0:03d}".format(i))
    logger.debug("Testing {} as target directory.".format(target_dir))
    if not os.path.exists(target_dir):
      logger.debug("{} does not exist yet! Using it!".format(target_dir))
      break

  if adb_wrapper.pull(source, target_dir):
    return target_dir
  return None


def classify_directory_using_build_fingerprint(adb_wrapper,
                                               source,
                                               results_dir,
                                               logger):
  """Decides which directory in results/ to dump new result to.

  This is a renewed method that makes use of build fingerprint to do
  result classification. This method allows for better usability when a user
  is obtaining many results for the same device or build.

  Args:
    adb_wrapper: An AdbWrapper instance.
    source: the source directory (on target device) containing new results.
    results_dir: the umbrella results/ directory.
    logger: A logger object to log debug or error messages.

  Returns:
    A string representing the final directory (on host) where results are pulled
    to. <code>None</code> is returned if any failure is encountered along the
    way.
  """
  # need to grab the build.txt to a tmp location
  tmp_file = "/tmp/device_build.txt"
  adb_pull_failed = False
  if not adb_wrapper.pull("{}/build.txt".format(source), tmp_file):
    logger.error("Failed to pull build.txt from device results dir.")
    adb_pull_failed = True

  if adb_pull_failed:
    logger.debug("Attempting to grab files using adb backup instead...")
    compressed_backup_filepath = "/tmp/hubble_results.ab"
    decompressed_backup_filepath = "/tmp/hubble_results.tar"
    logger.warning("Manual intervention required: Please select "
                   "`Back up my data` to proceed")
    if not adb_wrapper.backup(compressed_backup_filepath, HUBBLE_PACKAGE_NAME):
      logger.error("Failed to use `adb backup` to pull result files.")
      return None

    # now we have to decompress the backup file
    logger.debug("Reading from %s into buffer...", compressed_backup_filepath)
    compressed_backup_filecontent = ""
    with open(compressed_backup_filepath, "rb") as f_in:
      compressed_backup_filecontent = f_in.read()

    # we'll need to skip 24 bytes to skip the Android backup header
    logger.debug("Decompressing backup file...")
    decompressed_content = zlib.decompress(compressed_backup_filecontent[24:])
    with open(decompressed_backup_filepath, "wb") as f_out:
      f_out.write(decompressed_content)

    tarfile_obj = io.BytesIO(decompressed_content)
    tar_obj = tarfile.open(fileobj=tarfile_obj)
    base_result_path = "/tmp/untarred_hubble_results"
    logger.debug("Extracting result files into %s", base_result_path)
    tar_obj.extractall(base_result_path)

    # we overwrite tmp_file to reuse existing logic
    tmp_file = os.path.join(base_result_path, "apps", HUBBLE_PACKAGE_NAME,
                            "ef", "results", "build.txt")
    logger.debug("tmp_file: %s", tmp_file)

  build_info = ""
  with open(tmp_file, "r") as f_in:
    build_info = f_in.read()
  build_json = json.loads(build_info)
  build_fingerprint = build_json["buildInfo"][0]["fingerprint"]
  target_dir_parent = os.path.join(results_dir, build_fingerprint)

  if not os.path.exists(target_dir_parent):
    logger.debug("{} does not exist yet. Creating...".format(target_dir_parent))
    os.makedirs(target_dir_parent)

  target_dir = ""
  for i in range(1000):
    target_dir = os.path.join(target_dir_parent, "{0:03d}".format(i))
    logger.debug("Testing {} as target directory.".format(target_dir))
    if not os.path.exists(target_dir):
      logger.debug("{} does not exist yet! Using it!".format(target_dir))
      break

  if adb_pull_failed:
    source_dir = os.path.join("/tmp/untarred_hubble_results/apps",
                              HUBBLE_PACKAGE_NAME,
                              "ef",
                              "results")
    shutil.move(source_dir, target_dir)
    return target_dir
  else:
    if adb_wrapper.pull(source, target_dir):
      return target_dir
  return None


def extract_results(adb_wrapper, source, destination,
                    device, logger, use_old_classification=False):
  """Extracts results from Hubble's execution.

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    source: the path to where results live (on device).
    destination: the path to where results should be copied to (on host).
    device: A DeviceInfo object containing the device to operate on.
    logger: A logger object to log debug or error messages.
    use_old_classification: A boolean indicating whether to revert to old
                            classification of result, i.e. based on how ADB
                            displays device information. This is defaulted to
                            False.

  Returns:
    A string representing the final directory (on host) where results are copied
    to. <code>None</code> is returned if any failure is encountered along the
    way.
  """
  results_dir = None
  if not destination:
    results_dir = os.path.join(os.getcwd(), "results")
  else:
    # filter user supplied path via normpath to eliminate trailing slash(es)
    basename = os.path.basename(os.path.normpath(destination))
    if basename != "results":
      results_dir = os.path.join(destination, "results")
    else:
      results_dir = destination

  results_dir = os.path.abspath(results_dir)
  logger.debug("results_dir: {}".format(results_dir))
  if os.path.exists(results_dir):
    if not os.path.isdir(results_dir):
      logger.error("Supplied (--output) path is invalid.")
      return None
  else:
    logger.debug("{} does not exist yet. Creating...".format(results_dir))
    os.makedirs(results_dir, exist_ok=True)

  if use_old_classification:
    return classify_directory_using_adb_format(adb_wrapper, source, results_dir,
                                               device, logger)

  return classify_directory_using_build_fingerprint(adb_wrapper,
                                                    source,
                                                    results_dir,
                                                    logger)


def extract_selinux_policies(adb_wrapper, target_dir, logger):
  """Extracts SELinux policies from the device.

  Args:
    adb_wrapper: An AdbWrapper object that is used to issue ADB commands.
    target_dir: The path (on host) where results currently reside.
    logger: A logger object to log debug or error messages.
  """
  source_folders = [
      "/system/etc/selinux",
      "/vendor/etc/selinux"
  ]
  target_folders = [
      os.path.join(target_dir, "selinux/system/"),
      os.path.join(target_dir, "selinux/vendor/")
  ]

  logger.debug("Creating system and vendor target folders...")
  for folder in target_folders:
    os.makedirs(folder, exist_ok=True)

  for i in range(len(source_folders)):
    ls_cmd = ["ls", source_folders[i]]
    if not adb_wrapper.shell(ls_cmd):
      logger.warning("Failed to fully stat contents in %s", source_folders[i])

    # I'm forced to copy item-by-item because sometimes, the entire 'pull'
    # operation when one file fails to be copied, leaving other files that can
    # be copied uncopied.
    # NOTE that this doesn't yet deal with errors that occur more than 1 level
    # deep!
    for item in adb_wrapper.get_result():
      source_location = os.path.join(source_folders[i], item)
      logger.debug("source_location: %s", source_location)
      status = adb_wrapper.pull(source_location, target_folders[i])
      if not status:
        logger.warning("Failed to pull %s. Continuing...", source_location)


def main():
  args = parse_arguments()
  logger = set_up_logging(args)

  if not supported_platform(logger):
    logger.error("Sorry, your OS is currently unsupported for this script.")
    return

  if not verify_hubble(args, logger):
    return

  if not adb_installed(logger):
    return

  if not AdbWrapper.start_server(logger):
    return

  connected_devices = AdbWrapper.devices(logger)
  if not connected_devices or len(connected_devices) < 1:
    logger.error("No devices connected!")
    return

  logger.debug("There are %d connected device(s)", len(connected_devices))
  # Here we'll handle the cases where there can be 1 or more devices connected
  if len(connected_devices) > 1:
    # TODO(billylau): Branch off based on user-input - do all, or selectively
    logger.warning("More than 1 device connected!")

  results = {}
  for target_device in connected_devices:
    if target_device.unauthorized:
      logger.error("Please authorize device with serial number %s for ADB via "
                   "device GUI.", target_device.serial_number)
      continue

    # set up an adb_wrapper to be used throughout for this target device
    adb_wrapper = AdbWrapper(target_device.serial_number, logger)

    if is_hubble_installed(adb_wrapper, logger):
      logger.debug("Removing previous Hubble installation...")
      if not remove_previous_installation(adb_wrapper):
        logger.error("Failed to remove previous Hubble installation.")
        continue

    if is_xiaomi_phone(adb_wrapper, logger):
      logger.info("This is a Xiaomi phone.")
      adb_push_hubble(adb_wrapper, args.hubble)
      if not launch_xiaomi_file_explorer(adb_wrapper):
        logger.error("Failed to launch Xiaomi file explorer")
      while not is_hubble_installed(adb_wrapper, logger):
        logger.warning("Please manually install Hubble by launching the "
                       "\"Files Manager\" app (it may have been launched "
                       "for you) and navigate to the \"Downloads\" folder.")
        input("Press [ENTER] when you are done.")
    else:
      logger.info("This is not a Xiaomi phone. Regular workflow continues...")
      if not install_hubble(adb_wrapper, args, logger):
        logger.error("Error installing Hubble: %s", args.error_message)
        continue
    clear_logcat(adb_wrapper)

    if not launch_hubble(adb_wrapper):
      logger.error("Failed to launch Hubble: %s", adb_wrapper.error_message)
      continue
    results_source = wait_for_results(adb_wrapper, logger)

    if not results_source:
      logger.error("Failed to obtain results from Hubble execution.")
      continue
    use_old_classification = args.use_old_results_classification is not None
    results_dir = extract_results(adb_wrapper, results_source, args.output,
                                  target_device, logger, use_old_classification)

    if not results_dir:
      logger.error("Failed to extract results from target device (%s).",
                   target_device.serial_number)

    extract_selinux_policies(adb_wrapper, results_dir, logger)
    results[target_device.serial_number] = results_dir

  for device in results:
    logger.info("SUCCESS! Hubble was successfully deployed and executed on "
                "connected device %s.", device)
    logger.info("Hubble output files can be found at: %s", results[device])


if __name__ == "__main__":
  main()
