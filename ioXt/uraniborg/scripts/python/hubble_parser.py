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

"""Hubble Output Parser.

This module defines objects and methods that parse outputs from Hubble
observations and expose them as method calls to its consumers.
"""

import base64
import json
import os


class HubbleParser:
  """Hubble Parser.

  This class contains methods to read in outputs (assumed to be in JSON form)
  from Hubble observations and parses them into data structures that
  we make directly accessible via attributes or method calls. There are also
  nice helper methods to do printouts when we are measuring baseline and
  curating our baseline packages.
  """
  EXPECTED_VERSION = "1.0.0"

  # These are core files and their corresponding filenames that Hubble outputs,
  # which may expand in the future.
  # NOTE: It is imperative that for maintenance purposes, the key name does
  # not change in the future.
  CORE_FILES_DICT = {
      "packages": "packages.txt",
      "certificates": "certificates.txt",
      "device-properties": "device_properties.txt",
      "build": "build.txt",
      "hardware": "hardware.txt"
  }

  SYSTEM_SHARED_UID_SET = set([
      "android.uid.system",
      "android.uid.phone",
      "android.uid.log",
      "android.uid.nfc",
      "android.uid.bluetooth",
      "android.uid.shell",
      "android.uid.se",
      "android.uid.networkstack",
  ])

  @staticmethod
  def check_version(version_str):
    version_str_comps = version_str.split(".")
    expected_version_comps = HubbleParser.EXPECTED_VERSION.split(".")
    for i in range(2):
      if int(version_str_comps[i]) < int(expected_version_comps[i]):
        return False
    return True

  def __init__(self, logger, normalize=False):
    self.packages = ""
    self.certificates = ""
    self.device_properties = ""
    self.build = ""
    self.hardware = ""
    self.scorer = None
    self.normalize = normalize
    self.logger = logger
    self._platform_signature = ""
    self._shared_uid_packages = None

    # do a bit of sanity check
    logger.debug("normalize: %s", self.normalize)

  def parse_hubble_json(self, packages, build, hardware):
    """Consumes hubble output as json format.

    Args:
      packages: the json content from packages.txt of hubble output.
      build: the json content from build.txt of hubble output.
      hardware: the json content from hardware.txt of hubble output.

    Returns:
      True if json content is valid.
      False if not.
    """
    if not packages or not build or not hardware:
      return False
    self.packages = pacakges
    self.build = build
    self.hardware = hardware

  def parse_hubble_output(self, directory):
    """Consumes all hubble output and parses them into Python data structure.

    Assuming that hubble output files aren't renamed, this method is able to
    pick up the necessary files to be read into memory and loads them as JSON.

    Args:
      directory: the directory where hubble outputs for a specific observation
                 lives.

    Returns:
      True if every core file is parsed correctly.
      False if any error occurs along the way.
    """
    logger = self.logger
    output_files = os.listdir(directory)
    if not output_files:
      logger.error("%s is empty!", directory)
      return False

    for f in list(HubbleParser.CORE_FILES_DICT.values()):
      if not os.path.exists(os.path.join(directory, f)):
        logger.error("%s not found in %s", f, directory)
        return False

    if not self.parse_packages(os.path.join(
        directory, HubbleParser.CORE_FILES_DICT.get("packages"))):
      logger.error("Failed to parse packages.txt")
      return False
    if not self.parse_certificates(os.path.join(
        directory, HubbleParser.CORE_FILES_DICT.get("certificates"))):
      logger.error("Failed to parse certificates.txt")
      return False
    if not self.parse_device_properties(os.path.join(
        directory, HubbleParser.CORE_FILES_DICT.get("device-properties"))):
      logger.error("Failed to parse device_properties.txt")
      return False
    if not self.parse_build(os.path.join(
        directory, HubbleParser.CORE_FILES_DICT.get("build"))):
      logger.error("Failed to parse build.txt")
      return False
    if not self.parse_hardware(os.path.join(
        directory, HubbleParser.CORE_FILES_DICT.get("hardware"))):
      logger.error("Failed to parse hardware.txt")
      return False
    return True

  def get_oem(self):
    return self.hardware["oem"]

  def get_api_level(self):
    return self.build["apiLevel"]

  def get_shared_uid_packages(self):
    if not self._shared_uid_packages:
      platform_signature = self.get_platform_signature()
      self._shared_uid_packages = dict()
      for package in self.packages:
        if platform_signature in package["certIds"]:
          package_shared_uid = package["sharedUserId"]
          if package_shared_uid is not None:
            other_packages = self._shared_uid_packages.get(package_shared_uid)
            if other_packages is None:
              other_packages = [package["name"]]
            else:
              other_packages.append(package["name"])
            self._shared_uid_packages[package_shared_uid] = other_packages

    return self._shared_uid_packages


  def get_platform_signature(self):
    if not self._platform_signature:
      for package in self.packages:
        if package["name"] == "android":
          self._platform_signature = package["certIds"][0]

    return self._platform_signature

  def get_all_packages(self, get_codes_only):
    result = []
    for package in self.packages:
      if not get_codes_only or package["hasCode"]:
        result.append(package["name"])
    return result

  def get_platform_packages(self, get_codes_only):
    result = []
    platform_signature = self.get_platform_signature()
    for package in self.packages:
      if platform_signature in package["certIds"]:
        if not get_codes_only or package["hasCode"]:
          result.append(package["name"])
    return result

  def print_all_packages(self, print_codes_only):
    for package in self.packages:
      if not print_codes_only or package["hasCode"]:
        print("        \"{}\",".format(package["name"]))

  def print_platform_packages(self, print_codes_only):
    platform_signature = self.get_platform_signature()
    for package in self.packages:
      if platform_signature in package["certIds"]:
        if not print_codes_only or package["hasCode"]:
          print("        \"{}\",".format(package["name"]))

  def print_nocode_packages(self):
    for package in self.packages:
      if not package["hasCode"]:
        print("          \"{}\",".format(package["name"]))

  def read_in_json(self, path):
    logger = self.logger
    buff = ""
    with open(path, "r") as f_in:
      buff = f_in.read()
    info = json.loads(buff)
    if not HubbleParser.check_version(info["version"]):
      logger.error("Hubble output version is incompatible for %s", path)
      return None
    return info

  def parse_json(self, path, item_type):
    item = self.read_in_json(path)
    if not item:
      return False
    return item[item_type]

  def parse_packages(self, packages_path):
    packages = self.parse_json(packages_path, "packages")
    if not packages:
      return False
    self.packages = packages
    return True

  def parse_certificates(self, certs_path):
    certs = self.parse_json(certs_path, "certs")
    if not certs:
      return False
    self.certificates = certs
    return True

  def parse_device_properties(self, dev_prop_path):
    dev_props = self.parse_json(dev_prop_path, "b64EncodedDeviceProps")
    if not dev_props:
      return False
    encoded_props = dev_props[0]["encodedDevProps"]
    self.device_properties = base64.b64decode(encoded_props)
    return True

  def parse_build(self, build_path):
    build = self.parse_json(build_path, "buildInfo")
    if not build:
      return False
    self.build = build[0]
    return True

  def parse_hardware(self, hw_path):
    hw = self.parse_json(hw_path, "hwInfo")
    if not hw:
      return False
    self.hardware = hw[0]
    return True
