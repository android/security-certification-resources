#!/usr/bin/python
# Copyright 2019 Uraniborg authors.
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

"""AndroidManifest.xml Parser.

This script parses AndroidManifest.xml to extract permissions based on desired
protectionFlag for purpose of further analysis.
"""
from __future__ import print_function

import argparse
import logging
import sys

import xml.etree.cElementTree as ElementTree


NAMESPACE = {
    "android": "http://schemas.android.com/apk/res/android"
}


def parse_arguments():
  """Parses arguments passed in via cmd line.

  This function parses the various options that can be specified by the user
  when using this script and modifies internal states accordingly to reflect
  that when deciding what type of permissions to parse, and where to output
  the result to.

  Returns:
    A populated namespace object that contains appropriate options passed
    in by the user.
  """
  parser = argparse.ArgumentParser(
      description="Parse an AndroidManifest.xml file to mine for all declared "
                  "permissions. Note that permissions may be added/removed "
                  "per device, which is why we require an input file. The "
                  "output is always alphabetically sorted for consistency.")
  required_args = parser.add_argument_group("required arguments")
  required_args.add_argument("-i", "--input", required=True,
                             help="the AndroidManifest.xml file to be parsed.")

  parser.add_argument("-o", "--output", default="stdout",
                      help="the file to dump parsed permissions to")
  parser.add_argument("-D", "--debug", required=False, action="count",
                      help="If specified, debugging is turned on.")
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
  s_format = logging.Formatter("%(levelname)s:%(funcName)s(): %(message)s")
  s_handler.setFormatter(s_format)

  logger.addHandler(s_handler)
  return logger


def get_output_file(args, logger):
  logger.debug("args.output = {}".format(args.output))
  if not args.output or args.output == "stdout":
    outfile = sys.stdout
  elif args.output == "stderr":
    outfile = sys.stderr
  else:
    outfile = open(args.output, "w")
  return outfile


def parse_xml(args, logger):
  logger.debug("Parsing xml...")
  tree = ElementTree.parse(args.input)
  root = tree.getroot()
  return root


def parse_permissions(logger, xml_root):
  """Extracts the permissions defined through the XML body.

  Args:
    logger: the logger object to be used for logging within the function.
    xml_root: the root of the XML body.

  Returns:
    A dictionary of "protection level" as key, and a list of permissions at
    the protection level as the value.
  """
  permissions = xml_root.findall(".//permission")
  logger.info("There are {} permissions in this release.".format(
      len(permissions)))

  result = {}
  for permission in permissions:
    permission_name = permission.get("{{{}}}name".format(NAMESPACE["android"]))
    logger.debug("permission_name: {}".format(permission_name))
    prot_level = permission.get("{{{}}}protectionLevel".format(
        NAMESPACE["android"]))
    logger.debug("prot_level: {}".format(prot_level))

    if prot_level in result.keys():
      curr_set = result[prot_level]
      curr_set.add(permission_name)
      result[prot_level] = curr_set
    else:
      result[prot_level] = {permission_name}

  return result


def main():
  args = parse_arguments()
  logger = set_up_logging(args)

  ofile = get_output_file(args, logger)
  xml_root = parse_xml(args, logger)

  permissions_dict = parse_permissions(logger, xml_root)
  exclusive_prot_level = set()
  ofile.write("Permission Name,Protection Level\n")
  total_permissions = 0
  for prot_level in permissions_dict:
    logger.info("There are %d permissions of level: %s",
                len(permissions_dict[prot_level]), prot_level)
    for pl in prot_level.split("|"):
      exclusive_prot_level.add(pl)

    for perm in permissions_dict[prot_level]:
      total_permissions += 1
      ofile.write("{},{}\n".format(perm, prot_level))

  logger.info("There are %d exclusive protection levels in this release.",
              len(exclusive_prot_level))
  for pl in exclusive_prot_level:
    logger.info("Prot Level: %s", pl)

  logger.info("There are a total of %d permissions in this release.",
              total_permissions)
  ofile.close()

if __name__ == "__main__":
  main()
