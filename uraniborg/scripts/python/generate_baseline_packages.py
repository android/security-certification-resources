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

"""Generate Baseline Packages.

This is a script that can be used to generate packages as baseline or reference
to be used as point of comparisons with target builds/devices.

The output can be piped to separate files. This script is currently used to
generate BasePackages in package_whitelists module.

Currently, it reads in Hubble results directly and uses HubbleParser to parse
them and then classify them in the combination of whether they:
1. are platform signed
2. have code or not (purely resource APKs)
"""

import json
import logging
import os
import sys

import hubble_parser

HubbleParser = hubble_parser.HubbleParser


def set_up_logging(logging_level):
  logger = logging.getLogger(__name__)
  logger.setLevel(logging_level)
  log_format = ("%(levelname)s:%(filename)s:%(funcName)s(%(lineno)d): "
                "%(message)s")
  s_handler = logging.StreamHandler()
  s_format = logging.Formatter(log_format)
  s_handler.setFormatter(s_format)
  logger.addHandler(s_handler)
  return logger


def main():
  logger = set_up_logging(logging.ERROR)
  if len(sys.argv) < 2:
    logger.error("Insufficient argument!")
    print("USAGE: {} [directory]".format(sys.argv[0]))
    sys.exit(1)

  directory = os.path.join(sys.argv[1])

  hubble = HubbleParser(logger, False)
  if not hubble.parse_hubble_output(directory):
    logger.error("Failed to parse Hubble result files in %s", sys.argv[1])
    sys.exit(1)

  # List all packages
  packages_all = hubble.get_all_packages(False)
  packages_no_code = list(set(packages_all) -
                          set(hubble.get_all_packages(True)))

  # List all platform signed apps
  platform_signed_apps_all = hubble.get_platform_packages(False)
  platform_signed_no_code = list(set(platform_signed_apps_all) -
                                 set(hubble.get_platform_packages(True)))

  # Extract/Identify all shared uid apps
  shared_uid_apps = hubble.get_shared_uid_packages()

  # Create JSON Object
  data = {}
  data["packagesAll"] = packages_all
  data["packagesNoCode"] = packages_no_code
  data["platformAppsAll"] = platform_signed_apps_all
  data["platformAppsNoCode"] = platform_signed_no_code
  data["packagesSharedUid"] = shared_uid_apps
  json_data = json.dumps(data, indent=2)
  print(json_data)

if __name__ == "__main__":
  main()
