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

"""Device Scorer.

This script is an example program of how to make use of the risk_analyzer
module in order to compute a device's preloaded risk score.
"""

import argparse
import logging
import os
import sys

import hubble_parser
import package_whitelists
import risk_analyzer as RA

HubbleParser = hubble_parser.HubbleParser
BaselinePackages = package_whitelists.BaselinePackages
PackageWhitelists = package_whitelists.PackageWhitelists

# consts
KEY_PLATFORM = "platform"
KEY_RISKY_PERMS = "risky-permissions"
KEY_CLEARTEXT = "cleartext"


def parse_arguments():
  """Parses cmdline arguments.

  Returns:
    An object containing information about arguments that are passed within
    this namespace.
  """
  parser = argparse.ArgumentParser(
      description="A script that computes risk scores, starting with base "
                  "risk score computed based on risky permissions granted and "
                  "number of apps signed with platform signature."
                  "Secondary score is computed using a baseline, i.e. GSI.")
  required_args = parser.add_argument_group("required arguments")
  required_args.add_argument("--dir", required=True,
                             help="The directory that contains output files of "
                             "Hubble's observations.")

  parser.add_argument("-o", "--output", required=False, default=None,
                      help="Specifies the output directory where the "
                      "\"results\" directory can be found.")
  parser.add_argument("-D", "--debug", required=False, action="count",
                      help="If specified, debugging mode is turned on.")
  parser.add_argument("--normalize", required=False, action="count",
                      help="If specified, normalize score to use "
                      "GSI packages as filter.")
  parser.add_argument("--csv", required=False, action="count",
                      help="If specified, prints result in CSV form and no "
                      "logging messages will be printed.")
  parser.add_argument("--csv_header", required=False, action="count",
                      help="If specified together with --csv, headers will be"
                      "printed.")
  parser.add_argument("--include-gms",
                      action="store_true",
                      help="If specified, the calculation would include GMS "
                      "packages from risk calculation.")
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

  if args.csv:
    logger.setLevel(logging.CRITICAL)

  s_handler = logging.StreamHandler()
  log_format_w_filename = "%(levelname)s:%(filename)s:%(funcName)s(%(lineno)d): %(message)s"
  log_format_no_filename = "%(levelname)s:%(funcName)s(%(lineno)d): %(message)s"
  s_format = logging.Formatter(
      log_format_no_filename)
  s_handler.setFormatter(s_format)

  logger.addHandler(s_handler)
  return logger


def supported_platform(logger):
  """Check if this script is running on a supported platform.

  Args:
    logger: A valid logger instance to log error messages.

  Returns:
    True if this platform is supported.
  """
  # TODO(billy): Look into what is required to support Windows in the future.
  logger.debug("Current platform: {}".format(sys.platform))
  if not (sys.platform == "linux" or sys.platform == "darwin"):
    logger.error("Sorry, your OS is currenly unsupported for this script.")
    return False

  # Do this check to prevent users running this on Python2
  if not (sys.version_info.major == 3 and sys.version_info.minor >= 5):
    logger.error("This script requires Python 3.5 or higher!")
    logger.error("You are using Python {}.{}.".format(sys.version_info.major,
                                                      sys.version_info.minor))
    return False

  return True


def compute_scores(logger, hubble, normalize=False, include_gms=False):
  """Computes the scores for all the risk categories.

  Args:
    logger: A valid logger instance.
    hubble: A valid HubbleParser instance.
    normalize: A boolean indicating to normalize against baseline build or not.
               Usually this would be set to False if you want to do reading on
               a baseline build.
    include_gms: A boolean indicating whether or not to include GMS packages in
                 the computation. To ignore/discard GMS packages, set this to
                 False.
  Returns:
    A dict containing all the individual scores for every metric, depending on
    the inclusion or exclusion of GMS apps based on param include_gms.
  """
  results = dict()
  ### PLATFORM SIGNATURE RISK ########
  platform_signature_risk_analyzer = RA.PlatformSignature.get_scorer(
            hubble.get_api_level(), logger)
  platform_signature_risk = platform_signature_risk_analyzer.compute_score(
      hubble, normalize)
  logger.info("Platform Signature Risk: %2.4f / %2.4f", platform_signature_risk,
              platform_signature_risk_analyzer.PHI)
  results[KEY_PLATFORM] = platform_signature_risk

  logger.debug("include_gms: %s", include_gms)
  #### RISKY PRIVILEGED PERMISSIONS ##########
  if include_gms:
    risky_permissions_risk_analyzer = RA.RiskyPermissions.get_scorer(
        hubble.get_api_level(), logger)
    results[KEY_RISKY_PERMS] = risky_permissions_risk_analyzer.compute_score(
        hubble, normalize)
    logger.info("Pregranted Permissions Risk Score: %2.4f / %2.4f",
                results[KEY_RISKY_PERMS], risky_permissions_risk_analyzer.PHI)
  else:
    # without GMS
    risky_permissions_ra_no_gms = RA.RiskyPermissions.get_scorer(
        hubble.get_api_level(), logger, True)
    results[KEY_RISKY_PERMS] = risky_permissions_ra_no_gms.compute_score(
        hubble, normalize)
    logger.info("Pregranted Permissions Risk Score without GMS: %2.4f / %2.4f",
                results[KEY_RISKY_PERMS],
                risky_permissions_ra_no_gms.PHI)

  ### CLEARTEXT TRAFFIC #####
  if include_gms:
    cleartext_traffic_risk_analyzer = RA.CleartextTraffic.get_scorer(
        hubble.get_api_level(), logger)
    results[KEY_CLEARTEXT] = cleartext_traffic_risk_analyzer.compute_score(
        hubble, normalize)
    logger.info("Cleartext Traffic Risk Score: %2.4f / %2.4f",
                results[KEY_CLEARTEXT],
                cleartext_traffic_risk_analyzer.PHI)
  else:
    # without GMS
    cleartext_traffic_ra_no_gms = RA.CleartextTraffic.get_scorer(
        hubble.get_api_level(), logger, True)
    results[KEY_CLEARTEXT] = cleartext_traffic_ra_no_gms.compute_score(
        hubble, normalize)
    logger.info("Cleartext Traffic Risk Score without GMS: %2.4f / %2.4f",
                results[KEY_CLEARTEXT], cleartext_traffic_ra_no_gms.PHI)

  return results


def interpret_device_score(score):
  """Converts a device risk score into rating.

  Note that this is just an example of how scores can be divided into different
  rating categories, and does not represent the actual final rating that may be
  adopted by a more official standard.

  Args:
    score: A float representing the total device risk score.

  Returns:
    A string representing the interpretation or risk rating of the score.
  """
  if score >= 0 and score <= 2.5:
    return "VERY LOW"
  elif score > 2.5 and score <= 4.5:
    return "LOW"
  elif score > 4.5 and score <= 5.5:
    return "MEDIUM"
  elif score > 5.5 and score <= 6.5:
    return "HIGH"
  elif score > 6.5 and score <= 7.5:
    return "VERY HIGH"
  elif score > 7.5 and score <= 10.0:
    return "ELEVATED"
  return "ERROR"


def main():
  args = parse_arguments()
  logger = set_up_logging(args)

  if not supported_platform(logger):
    logger.error("Sorry, your OS is currently unsupported for this script.")
    return

  logger.debug("normalize: %s", args.normalize)
  logger.debug("include gmscore: %s", args.include_gms)

  # read in Hubble's output
  directory = args.dir
  if not os.path.exists(directory):
    logger.error("%s does not exist!", directory)
    return

  normalize = args.normalize is not None

  hp = HubbleParser(logger, normalize)
  if not hp.parse_hubble_output(args.dir):
    logger.error("Error parsing necessary files. Aborting...")
    return

  scores = compute_scores(logger, hp, normalize, args.include_gms)
  total_risk_score = sum(scores.values())
  if args.include_gms:
    logger.warning("Uraniborg's Device Preloaded Apps Risk Score "
                   "(including GMS): {:2.2f} / 10.00".format(total_risk_score))
  else:
    logger.warning("Adjusted Uraniborg's Device Preloaded Apps Risk Score "
                   "(without GMS): {:2.2f} / 10.00".format(total_risk_score))
    logger.warning("Use the --include-gms option to see the scores with GMS")

  if args.csv:
    device_info_str = "{},{}:{}:{},{},{},".format(hp.hardware["oem"],
                                                  hp.hardware["brand"],
                                                  hp.hardware["modelName"],
                                                  hp.hardware["productName"],
                                                  hp.build["apiLevel"],
                                                  hp.build["fingerprint"])
    scores_str = "{:2.2f},{:2.2f},{:2.2f},".format(scores[KEY_PLATFORM],
                                                   scores[KEY_RISKY_PERMS],
                                                   scores[KEY_CLEARTEXT])
    scores_str += "{:2.2f},".format(total_risk_score)
    rating_str = "{},".format(interpret_device_score(total_risk_score))
    print(device_info_str + scores_str)


if __name__ == "__main__":
  main()
