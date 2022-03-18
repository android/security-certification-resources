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

"""System call wrapper.

Defines classes that wraps around direct calls via Python's Popen interface.
Also, AdbWrapper that wraps around the type of ADB calls that are used by the
other scripts.
"""

import logging
import subprocess


class SyscallWrapper:
  """A class that wraps around different modes of doing os system calls.
  """

  def __init__(self, logger):
    self._logger = logger
    self.reset()

  def reset(self):
    self.last_command = ""
    self.error_occured = False
    self.error_message = ""
    self.intermediate_result = []
    self.intermediate_error = []
    self.result_final = []
    self.return_code = 0

  def call_returnable_command_ignore_output(self, cmd):
    """Makes calls that usually returns but ignores all results/errors.

    Example of what this might be suitable for is 'adb logcat -c'

    Args:
      cmd: The command to be executed.
    """
    self.reset()
    logger = self._logger
    logger.debug("cmd: {}".format(cmd))
    self.last_command = cmd

    cmd_pid = subprocess.Popen(cmd)
    while cmd_pid.poll() is None:
      pass

  def call_returnable_command(self, cmd):
    """Handles the type of calls that returns.

    For these type of calls, we can just pull the results when the call ends.

    Args:
      cmd: The command to be executed.
    """
    self.reset()
    logger = self._logger
    logger.debug("cmd: {}".format(cmd))
    self.last_command = cmd

    cmd_pid = subprocess.Popen(cmd, stdin=subprocess.PIPE,
                               stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    while True:
      self.return_code = cmd_pid.poll()
      try:
        line = cmd_pid.stdout.readline().decode("utf-8", "replace").strip()
        if line:
          self.result_final.append(line)
      except KeyboardInterrupt:
        self.error_occured = True
        self.error_message = "Terminated by keyboard interrupt."
        break
      if not line and self.return_code is not None:
        break

    if self.return_code == 0:
      return

    # try to extract error message from stderr
    logger.debug("return code: {}".format(self.return_code))
    self.error_occured = True
    self.error_final = []
    while True:
      try:
        line = cmd_pid.stderr.readline().decode("utf-8", "replace").strip()
        if line:
          self.error_final.append(line)
      except KeyboardInterrupt:
        self.error_message = ("Error message extraction interrupted by keyboard"
                              " interrupt:")
        break
      if not line:
        break
    self.error_message = "\n".join(self.error_final)
    return

  def call_non_returnable_command(self, cmd, terminate):
    """Handles the type of calls that do not return (e.g. adb logcat, tail).

    These type of calls are much more complicated, and rely upon correct
    implementation of a terminating function to tell us when to stop.

    TODO(billy): Consider accepting and handling a timeout value that terminates
    the execution of the command regardless of state so that we don't risk
    looping indefinitely.

    Args:
      cmd: The command to be executed.
      terminate: A function that determines if the call is to be terminated.
                 It must take a buffer, logger, and an outgoing buffer as
                 parameter.
    """
    self.reset()
    logger = self._logger
    logger.debug("cmd: {}".format(cmd))
    self.last_command = cmd

    cmd_pid = subprocess.Popen(cmd, stdin=subprocess.PIPE,
                               stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    while cmd_pid.poll() is None:
      try:
        line = cmd_pid.stdout.readline().decode("utf-8", "replace").strip()
        if line:
          self.intermediate_result.append(line)
          result = terminate(line, logger)
          if result:
            self.error_occured = False
            logger.debug("Found terminating condition... Result: %s", result)
            cmd_pid.kill()
            self.result_final.append(result)
            break
      except KeyboardInterrupt:
        self.error_occured = True
        self.error_message = "Execution interrupted by keyboard."
        cmd_pid.kill()
        break

    return


class AdbWrapper:
  """A class that wraps around various ADB calls.
  """

  BASE_ADB_COMMAND = ["adb"]

  def __init__(self, serial_number, logger=None):
    if not logger:
      self._logger = AdbWrapper.set_up_default_logger()
    else:
      self._logger = logger
      self.syscall_wrapper = SyscallWrapper(logger)

    self.device_serial_number = serial_number
    self.adb_command = AdbWrapper.BASE_ADB_COMMAND + ["-s", serial_number]
    self.adb_shell_command = self.adb_command + ["shell"]

  @staticmethod
  def set_up_default_logger():
    """Sets up the default logger for this class.

    Logging level is set to ERROR.

    Returns:
      A logger object.
    """
    logger = logging.getLogger(__name__)
    logger.setLevel(logging.WARNING)
    s_handler = logging.StreamHandler()
    s_format = logging.Formatter(
        "%(levelname)s:%(filename)s:%(funcName)s(%(lineno)d): %(message)s")
    s_handler.setFormatter(s_format)
    logger.addHandler(s_handler)
    return logger

  def get_result(self):
    return self.syscall_wrapper.result_final

  @staticmethod
  def start_server(logger=None):
    """Calls 'adb start-server' to start ADB server.

    Args:
      logger: An optional logger object used for logging.

    Returns:
      A boolean indicating the success of this operation.
    """
    if not logger:
      logger = AdbWrapper.set_up_default_logger()
    cmd = AdbWrapper.BASE_ADB_COMMAND[:]
    cmd.append("start-server")
    logger.debug("cmd: %s", cmd)
    sw = SyscallWrapper(logger)
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("Failed to start adb server: %s", sw.error_message)
      return False
    return True

  @staticmethod
  def devices(logger=None):
    """Calls 'adb devices -l' to list connected devices via ADB.

    Args:
      logger: An optional logger object used for logging.

    Returns:
      A list of DeviceInfo containing all connected devices.
      <code>None</code> is returned if error occurs.
    """
    if not logger:
      logger = AdbWrapper.set_up_default_logger()
    cmd = AdbWrapper.BASE_ADB_COMMAND[:]
    cmd.extend(["devices", "-l"])
    logger.debug("cmd: %s", cmd)
    sw = SyscallWrapper(logger)
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("Failure: %s", sw.error_final)
      return None

    if len(sw.result_final) < 2:
      logger.warning("No devices connected!")
      return []

    connected_devices = []
    for line in sw.result_final[1:]:
      logger.debug("line: %s", line)

      device = DeviceInfo()
      line_components = line.split()
      device.serial_number = line_components[0]

      if "unauthorized" in line:
        logger.warning("ADB has not been authorized for device with serial "
                       "number %s", device.serial_number)
        device.unauthorized = True
        connected_devices.append(device)
        continue

      for component in line_components:
        if component.find("product:") == 0:
          device.product_name = component.split(":")[1].strip()
        elif component.find("model:") == 0:
          device.model_name = component.split(":")[1].strip()
        elif component.find("device:") == 0:
          device.device_name = component.split(":")[1].strip()
        elif component.find("transport_id:") == 0:
          device.transport_id = component.split(":")[1].strip()
      connected_devices.append(device)

    return connected_devices

  def am_start(self, package_name, component_name,
               action_name=None, extra_string=None):
    """Calls 'adb am start ...'.

    Args:
      package_name: The package name to be invoked.
      component_name: The component name in the package to be directed to.
      action_name: The action name of the intent (usually in the form of
                    action.intent.SOME_ACTION)
      extra_string: The extra strings to pass along.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_shell_command[:]
    cmd.extend(["am", "start"])
    if action_name:
      cmd.extend(["-a", action_name])
      if extra_string:
        cmd.extend(["--es", extra_string[0], extra_string[1]])
    cmd.extend(["-n", "{}/{}".format(package_name, component_name)])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("%s failed: %s", cmd, sw.error_message)
      return False
    return True

  def logcat_clear(self):
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["logcat", "-c"])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command_ignore_output(cmd)
    return

  def logcat_find(self, patterns, terminating_function):
    """Helper function to find certain patterns in logcat.

    Args:
      patterns: A list containing regex/patterns to be passed to "adb logcat"
                command for logcat level filtering.
      terminating_function: A function pointer to a function that takes a buffer
                            as input parameter. When None is returned by the
                            terminating function, the process will be terminated
                            and control flow will return to the caller.

    Returns:
      A string representing the results obtained from terminating_function.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["logcat"])
    if patterns:
      cmd.extend(patterns)
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_non_returnable_command(cmd, terminating_function)
    if sw.error_occured:
      logger.error("Failed to find pattern in logcat: %s", sw.error_message)
      return None
    return sw.result_final[0]

  def install(self, path_to_apk):
    """Calls 'adb install APK'.

    Args:
      path_to_apk: A string representing the path to where the APK resides.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["install", path_to_apk])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("ADB install failed: %s", sw.error_message)
      return False
    return True

  def uninstall(self, package_name):
    """Calls 'adb uninstall package_name' to delete the package from device.

    Args:
      package_name: The name of the package to be deleted.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["uninstall", package_name])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("ADB uninstall %s failed: %s", package_name,
                   sw.error_message)
      return False
    return True

  def shell(self, cmd):
    logger = self._logger
    cmd = self.adb_shell_command + cmd
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("%s failed with message: %s", cmd, sw.error_message)
      return False
    return True

  def pull(self, source_path, target_path):
    """Calls 'adb pull' to download something from the device.

    Args:
      source_path: The path on device to pull from.
      target_path: The path on host to download results to.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["pull", source_path, target_path])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("Pulling from %s failed: %s", source_path, sw.error_message)
      return False
    return True

  def push(self, source_path, target_path):
    """Calls 'adb push' to upload something to the device.

    Args:
      source_path: The path on host containing dir or file to upload.
      target_path: The path on device indicating where to upload to.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["push", source_path, target_path])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("%s failed: %s", cmd, sw.error_message)
      return False
    return True

  def backup(self, backup_filepath, package_name):
    """Calls 'adb backup -f' to pull result files.

    Note that this is not a generic adb backup implementation.

    Args:
      backup_filepath: The path to save the backup file to.
      package_name: The name of the package to back-up.

    Returns:
      A boolean indicating the success of the operation.
    """
    logger = self._logger
    cmd = self.adb_command[:]
    cmd.extend(["backup", "-f", backup_filepath, package_name])
    logger.debug("cmd: %s", cmd)
    sw = self.syscall_wrapper
    sw.call_returnable_command(cmd)
    if sw.error_occured:
      logger.error("%s failed: %s", cmd, sw.error_message)
      return False
    return True


class DeviceInfo:
  """A class representing basic information about a physical device.
  """
  serial_number = ""
  product_name = ""
  model_name = ""
  device_name = ""
  transport_id = ""
  unauthorized = False
