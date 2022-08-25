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

"""Risk Analyzers.

This module defines a generic class that encapsulates the necessary
functionalities and attributes of a risk scorer.
Out of that, specific risk analyzers inherit from the generic class & implement
how scoring is done for each risk metric.
"""
import abc
import logging
import hubble_parser
import package_whitelists


ABC = abc.ABC
abstractmethod = abc.abstractmethod
HubbleParser = hubble_parser.HubbleParser
BaselinePackages = package_whitelists.BaselinePackages
PackageWhitelists = package_whitelists.PackageWhitelists
GMS = package_whitelists.GMS


class RiskAnalyzer(ABC):
  """A generic risk analyzer class.

  As this is an abstract class, it should not be instantiable; It defines a set
  of interfaces that its inheritors MUST implement.

  NOTE TO IMPLEMENTORS:
  If you wish to implement a new metric, you should create a new class that
  inherits from this class, and implement the abstract methods. For example,
  look at how the current metrics are implemented.
  """
  BASE_SCORE = 0
  PHI = 0
  LAMBDA_I = 0.25


  def __init__(self):
    self.related_apps = []

  def get_related_apps(self):
    """Returns a list of apps which are considered by the analyzer."""
    return self.related_apps

  @staticmethod
  def convert_to_odds(probability):
    return probability / (1 - probability)

  @staticmethod
  def convert_to_probability(odds):
    return odds / (1 + odds)

  @abstractmethod
  def compute_base_score(self, hubble, normalize):
    """This method must be implemented to calculate a raw score for a metric.

    If you are defining and implementing a new metric, you would want this
    method to do the base calculations.

    Args:
      hubble: A valid HubbleParser instance containing the output of a target
              build/device to be evaluated.
      normalize: A boolean indicating whether or not to do normalization against
                 a baseline build. Typically, this is set to True when you are
                 evaluating the additional signals by a target device, and set
                 to False when you are collecting data for baseline build.

    Returns:
      Either a float or an int (depending on what your scores look like) of an
      unweighted raw/base risk score for the metric you are defining.
    """
    pass

  @abstractmethod
  def compute_score(self, hubble, normalize):
    pass

  @staticmethod
  @abstractmethod
  def get_scorer(api_level, logger):
    """For callers to get an instance of a scorer according to API level.

    This method must be implemented by an inheriting class. Essentially this is
    similar to a factory method where an instance of a scorer is returned to the
    caller according to the API level that is passed in.

    Args:
      api_level: the API level of the build to be scored/compared against.
      logger: a valid logger instance.

    Returns:
      An instance of a scorer that can be used immediately. Else, exception may
      be raised if the API level is not handled/implemented yet.
    """
    pass

  def compute_lambda_o(self, iota):
    return iota * self.LAMBDA_I / (1 + self.LAMBDA_I * (iota - 1))

  def _billy_formula_v1(self, hubble, normalize):
    """Implements my initial formula.

    The formula for this is slightly more involved. Here, the risk score is
    defined as probability (lambda) x damage_potential (PHI). However, in order
    to calculate the observed probability (lambda_o), we use likelihood ratio
    (iota) and multiply that with the odds of occurence. Relationship between
    probability and odds are also defined in other methods in this class.
    Finally, we return the computed observed probability (lambda_o) and multiply
    that with the weight or damage potential (PHI) of the metric.

    Args:
      hubble: A HubbleParser instance containing the output of a target build to
              be evaluated.
      normalize: A boolean indicating whether or not to normalize the reading
                 with respect to baseline.

    Returns:
      A float representing the final (weighted) score of a metric.
    """
    logger = self.logger
    raw_score = self.compute_base_score(hubble, normalize)

    iota = raw_score / self.BASE_SCORE
    logger.debug("iota = %2.2f / %2.2f = %2.4f", raw_score, self.BASE_SCORE)
    lambda_o = self.compute_lambda_o(iota)
    logger.debug("lambda_o = %2.4f", lambda_o)
    return lambda_o * self.PHI

  def _cambridge(self, hubble, normalize):
    r"""Implements the Cambridge formula.

    The formula is simple: r = |A_t \ A_baseline| / |A_t| where:
    r is the likelihood of risk we are calculating for a particular metric,
    A_t is the set of apps on target device meeting the criteria for the metric,
    A_baseline is the set of apps on baseline build that meets the same criteria
    \ is the set complement operator.

    Args:
      hubble: A HubbleParser instance containing the output of target build to
              be evaluated.
      normalize: A boolean indicating whether or not to normalize the reading
                 with respect to baseline.

    Returns:
      A float representing the final (weighted) score of a metric.
    """
    logger = self.logger
    numerator = self.compute_base_score(hubble, True)
    denominator = self.compute_base_score(hubble, False)
    r = 0.0
    if denominator:
      r = numerator / denominator
    logger.debug("r = %2.2f / %2.2f = %2.4f", numerator, denominator, r)
    return r * self.PHI


class PlatformSignature(RiskAnalyzer):
  """The platform signature risk analyzer.

  This class analyzes packages on a particular build to score the risks posed
  by the introduction of additional platform signed packages. The comparison
  is made with a relevant baseline: either GSI or AOSP builds when available.
  """
  PHI = 6.0
  LAMBDA_I = 0.25

  def __init__(self, logger):
    super().__init__()
    self.logger = logger
    self.platform_apps = []

  @staticmethod
  def get_scorer(api_level, logger):
    if api_level == 33:
      logger.debug("Returning TPlatformSignature")
      return TPlatformSignature(logger)
    if api_level == 31:
      logger.debug("Returning SPlatformSignature")
      return SPlatformSignature(logger)
    if api_level == 30:
      logger.debug("Returning RPlatformSignature")
      return RPlatformSignature(logger)
    if api_level == 29:
      logger.debug("Returning QPlatformSignature")
      return QPlatformSignature(logger)
    elif api_level == 28:
      logger.debug("Returning PPlatformSignature")
      return PPlatformSignature(logger)
    elif api_level == 27 or api_level == 26:
      logger.debug("Returning OPlatformSignature")
      return OPlatformSignature(logger)
    elif api_level == 25 or api_level == 24:
      return NPlatformSignature(logger)
    else:
      raise Exception("API Level not handled!")

  def compute_base_score(self, hubble, normalize):
    """Computes the base score for platforn signature metrics.

    Args:
      hubble: A valid instance of HubbleParser object.
      normalize: A boolean indicating to normalize the score against baseline or
                 not. Typically, this value is set to True when computing score
                 for a target device and False when collecting base score for
                 baseline measurements.

    Returns:
      An int representing the raw score of this metrics
    """
    logger = self.logger

    # first determine platform signature
    platform_signature = hubble.get_platform_signature()

    num_platform_signed_app = 0
    packages = hubble.packages
    whitelist = PackageWhitelists.get_whitelist(hubble.get_oem())
    baseline = BaselinePackages.get_instance(hubble.get_api_level())
    hubble.platform_apps = []
    hubble.system_uid_apps = []
    update_apps = False
    if not self.related_apps and normalize:
      update_apps = True
    for package in packages:
      package_name = package["name"]

      ### do filtering
      if package_name in whitelist.EXCLUDED_PACKAGES:
        logger.debug("^%s is in OEM global whitelist. Excluding...",
                     package_name)
        continue

      if normalize:
        fuzzy_matched_package = PackageWhitelists.package_name_fuzzy_match(
            logger, package_name, baseline.get_platform_signed_packages())
        if fuzzy_matched_package:
          # only discount if the same package was signed using platform
          # signature in GSI's build
          continue

      if platform_signature in package["certIds"]:
        hubble.platform_apps.append(package_name)
        num_platform_signed_app += 1
        logger.debug("+%s is platform signed. Running score: %d", package_name,
                     num_platform_signed_app)

        if package["sharedUserId"] == "android.uid.system":
          hubble.system_uid_apps.append(package_name)

        if not package["hasCode"]:
          # we discount those that has no code, but not remove from our list,
          # because these packages are still signed with platform signature
          # nonetheless.
          num_platform_signed_app -= 1
          logger.debug("-%s but has no code. Running total: %d", package_name,
                       num_platform_signed_app)
        elif update_apps:
          self.related_apps.append(package)
    return num_platform_signed_app

  def compute_score(self, hubble, normalize):
    # This is where you can choose which scheme to use (including the one you
    # implemented yourself) to compute the weighted score for the metric you are
    # measuring.
    return self._cambridge(hubble, normalize)


# NOTE TO IMPLEMENTORS:
# BASE_SCORE is calculated by doing a run with normalize=False on baseline data.
#  But it is not used in the final risk formula today.
class NPlatformSignature(PlatformSignature):
  API_LEVEL = 25
  BASE_SCORE = 38


class OPlatformSignature(PlatformSignature):
  API_LEVEL = 27
  BASE_SCORE = 42


class PPlatformSignature(PlatformSignature):
  API_LEVEL = 28
  BASE_SCORE = 41


class QPlatformSignature(PlatformSignature):
  API_LEVEL = 29
  BASE_SCORE = 49


class RPlatformSignature(PlatformSignature):
  API_LEVEL = 30
  BASE_SCORE = 49


class SPlatformSignature(PlatformSignature):
  API_LEVEL = 31
  BASE_SCORE = 45


class TPlatformSignature(PlatformSignature):
  API_LEVEL = 33
  BASE_SCORE = 50


class SystemUid(RiskAnalyzer):
  """Analyzer for system uid risks.
  """
  PHI = 6.0
  LAMBDA_I = 0.25

  def __init__(self, logger):
    super().__init__()
    self.logger = logger
    self.system_uid_apps = []

  @staticmethod
  def get_scorer(api_level, logger):
    if api_level == 33:
      logger.debug("Returning TSystemUid")
      return TSystemUid(logger)
    if api_level == 31:
      logger.debug("Returning SSystemUid")
      return SSystemUid(logger)
    if api_level == 30:
      logger.debug("Returning RSystemUid")
      return RSystemUid(logger)
    if api_level == 29:
      logger.debug("Returning QSystemUid")
      return QSystemUid(logger)
    if api_level == 28:
      logger.debug("Return PSystemUid")
      return PSystemUid(logger)
    elif api_level == 27 or api_level == 26:
      logger.debug("Returning OSystemUid")
      return OSystemUid(logger)
    elif api_level == 25 or api_level == 24:
      return NSystemUid(logger)
    else:
      raise Exception("API Level not supported!")

  def compute_base_score(self, hubble, normalize):
    logger = self.logger

    shared_uid_packages = hubble.get_shared_uid_packages()
    baseline = BaselinePackages.get_instance(hubble.get_api_level())
    baseline_shared_uid_packages = baseline.get_shared_uid_packages()
    whitelist = PackageWhitelists.get_whitelist(hubble.get_oem())

    shared_uid_score = 0
    for shared_uid in shared_uid_packages:
      for package_name in shared_uid_packages[shared_uid]:

        if normalize:
          if package_name in whitelist.EXCLUDED_PACKAGES:
            logger.debug("^%s is in OEM global whitelist. Excluding...",
                         package_name)
            continue

          b_suid_packages = baseline_shared_uid_packages.get(shared_uid)
          if b_suid_packages is not None and package_name in b_suid_packages:
            logger.debug("^%s is a baseline shared uid: %s. Skipping...",
                         package_name, shared_uid)
            continue
        logger.debug("+%s is newly introduced shared uid: %s.", package_name,
                     shared_uid)
        shared_uid_score += 1
    logger.debug("Shared UID Score: %d", shared_uid_score)
    return shared_uid_score

  def compute_score(self, hubble, normalize):
    return self._cambridge(hubble, normalize)


class NSystemUid(SystemUid):
  API_LEVEL = 25
  BASE_SCORE = 17


class OSystemUid(SystemUid):
  API_LEVEL = 27
  BASE_SCORE = 19


class PSystemUid(SystemUid):
  API_LEVEL = 28
  BASE_SCORE = 17


class QSystemUid(SystemUid):
  API_LEVEL = 29
  BASE_SCORE = 23


class RSystemUid(SystemUid):
  API_LEVEL = 30
  BASE_SCORE = 24


class SSystemUid(SystemUid):
  API_LEVEL = 31
  BASE_SCORE = 25


class TSystemUid(SystemUid):
  API_LEVEL = 33
  BASE_SCORE = 30


class RiskyPermissions(RiskAnalyzer):
  """The risky permission risk analyzer.

  This class provides a stack ranked permissions according to the severity of
  the permissions. Then, it computes the score based on how many of the
  corresponding permissions are pre-granted to each app.
  """
  PHI = 3
  LAMBDA_I = 0.25

  class Weights:
    RISK_ASTRONOMICAL = 100
    RISK_CRITICAL = 10
    RISK_HIGH = 7.5
    RISK_MEDIUM = 5
    RISK_LOW = 2.5

  class Permissions:
    """Nested class of stack ranked permissions.

    The permissions are evaluated from the Android framework, and stack ranked
    according to the severity of the impact/risk of the permissions. This was
    done based on inputs from security experts.
    """
    RISK_ASTRONOMICAL = {
        "android.permission.INSTALL_PACKAGES"
    }

    RISK_CRITICAL = {
        "android.permission.COPY_PROTECTED_DATA",
        "android.permission.WRITE_SECURE_SETTINGS",
        "android.permission.READ_FRAME_BUFFER",
        "android.permission.MANAGE_CA_CERTIFICATES",
        "android.permission.MANAGE_APP_OPS_MODES",
        "android.permission.GRANT_RUNTIME_PERMISSIONS",
        "android.permission.DUMP",
        "android.permission.CAMERA",
        "android.permission.SYSTEM_CAMERA",
        "android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS",
        "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
    }

    RISK_HIGH = {
        "android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS",
        "android.permission.READ_SMS",
        "android.permission.WRITE_SMS",
        "android.permission.RECEIVE_MMS",
        "android.permission.SEND_SMS_NO_CONFIRMATION",
        "android.permission.RECEIVE_SMS",
        "android.permission.READ_LOGS",
        "android.permission.READ_PRIVILEGED_PHONE_STATE",
        "android.permission.LOCATION_HARDWARE",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_BACKGROUND_LOCATION",
        "android.permission.BIND_ACCESSIBILITY_SERVICE",
        "android.permission.ACCESS_WIFI_STATE",
        "com.android.voicemail.permission.READ_VOICEMAIL",
        "android.permission.RECORD_AUDIO",
        "android.permission.CAPTURE_AUDIO_OUTPUT",
        "android.permission.ACCESS_NOTIFICATIONS",
        "android.permission.INTERACT_ACROSS_USERS_FULL",
        "android.permission.BLUETOOTH_PRIVILEGED",
        "android.permission.GET_PASSWORD",
        "android.permission.INTERNAL_SYSTEM_WINDOW",
    }

    RISK_MEDIUM = {
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.CHANGE_COMPONENT_ENABLED_STATE",
        "android.permission.READ_CONTACTS",
        "android.permission.WRITE_CONTACTS",
        "android.permission.CONNECTIVITY_INTERNAL",
        "android.permission.ACCESS_MEDIA_LOCATION",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.SYSTEM_ALERT_WINDOW",
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG",
        "android.permission.INTERACT_ACROSS_USERS",
        "android.permission.MANAGE_USERS",
        "android.permission.READ_CALENDAR",
        "android.permission.BLUETOOTH_ADMIN",
        "android.permission.BODY_SENSORS",

    }

    RISK_LOW = {
        "android.permission.DOWNLOAD_WITHOUT_NOTIFICATION",
        "android.permission.PACKAGE_USAGE_STATS",
        "android.permission.MASTER_CLEAR",
        "android.permission.DELETE_PACKAGES",
        "android.permission.GET_PACKAGE_SIZE",
        "android.permission.BLUETOOTH",
        "android.permission.DEVICE_POWER",
    }

  @staticmethod
  def get_scorer(api_level, logger, google_discount=False):
    if api_level == 33:
      return TRiskyPermissions(logger, google_discount)
    if api_level == 31:
      return SRiskyPermissions(logger, google_discount)
    if api_level == 30:
      return RRiskyPermissions(logger, google_discount)
    if api_level == 29:
      return QRiskyPermissions(logger, google_discount)
    elif api_level == 28:
      return PRiskyPermissions(logger, google_discount)
    elif api_level == 27 or api_level == 26:
      return ORiskyPermissions(logger, google_discount)
    elif api_level == 25 or api_level == 24:
      return NRiskyPermissions(logger, google_discount)
    else:
      raise Exception("API Level not handled!")

  @staticmethod
  def map_permission_to_score(permission_name):
    """Maps permission to a score.

    Scores are determined based on their risk category. The value of each
    category is also pre-defined in RiskyPermissions.Weights

    Args:
      permission_name: a string containing permission name

    Returns:
      An int representing the score of the permission. If the permission
      is not found under any of the risk categories, 0 is returned.
    """
    Permissions = RiskyPermissions.Permissions
    Weights = RiskyPermissions.Weights
    permission_levels = [a for a in dir(Permissions) if a.startswith("RISK")]

    for permission_level in permission_levels:
      if permission_name in getattr(Permissions, permission_level):
        return getattr(Weights, permission_level)

    return 0

  def __init__(self, logger, google_discount):
    super().__init__()
    self.logger = logger
    self.google_discount = google_discount

  def _compute_fair_permission_score(self, permissions):
    """Account for permissions within the same group.

    The intuition behind this can be expressed via the following example. If an
    app is granted both ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION, it
    should be accounted for its more severe permission, i.e. the fine grained
    permission. It does not gain additional ability from the
    ACCESS_COARSE_LOCATION permission, and thus does not pose additional risk
    as a result too. Thus, our risk score computation should reflect cases like
    this.

    So far, we are handling location and sms as permission groups. This can be
    expanded in the future to include more categories.

    Args:
      permissions: A collection of strings representing Android permission names
                   to be scored.

    Returns:
      A dict containing scores in various categories. Most of the rest of the
      scores are accrued within the 'generic' key.
    """
    scores = dict()
    scores["generic"] = 0
    scores["location"] = 0
    scores["sms"] = 0

    for permission in permissions:
      if "LOCATION" in permission:
        scores["location"] = max(scores["location"],
                                 RiskyPermissions.map_permission_to_score(
                                     permission))
        continue
      if "SMS" in permission:
        scores["sms"] = max(scores["sms"],
                            RiskyPermissions.map_permission_to_score(
                                permission))
        continue

      scores["generic"] += RiskyPermissions.map_permission_to_score(permission)
    return scores

  def compute_base_score(self, hubble, normalize):
    logger = self.logger
    total_granted_score = 0
    total_ungranted_score = 0

    packages = hubble.packages
    whitelist = PackageWhitelists.get_whitelist(hubble.get_oem())
    baseline = BaselinePackages.get_instance(hubble.get_api_level())

    gms_packages = GMS.PACKAGES if self.google_discount else []
    update_apps = False
    if not self.related_apps and normalize:
      update_apps = True
    for package in packages:
      package_name = package["name"]

      # We start by handling whitelists or identifying packages that should
      # be skipped.
      if package_name in hubble.platform_apps:
        logger.debug("^%s was platform signed. Skipping...", package_name)
        continue

      if package_name in whitelist.EXCLUDED_PACKAGES:
        logger.debug("^%s is listed in OEM whitelist. Skipping...",
                     package_name)
        continue

      if package_name in whitelist.INSTALLER_PACKAGES:
        if package["certIds"][0] == whitelist.INSTALLER_PACKAGES.get(
            package_name):
          logger.debug("^%s is the OEM's app store. Skipping...", package_name)
          continue
        else:
          logger.warning("!%s's signature does not match whitelist",
                         package_name)

      if normalize:
        fuzzy_matched_package = PackageWhitelists.package_name_fuzzy_match(
            logger, package_name, baseline.get_all_packages())
        if fuzzy_matched_package:
          # the ideal case would be to compute the diff of permission between
          # packages. But we'll use a simpler version by skipping for now in
          # this iteration.
          logger.debug("^%s skipped - fuzzy matched to GSI package %s",
                       package_name, fuzzy_matched_package)
          continue

      if not package["hasCode"]:
        logger.debug("^%s has no code. Skipping...", package_name)
        continue

      if package_name in gms_packages:
        if GMS.is_gms_package(package):
          logger.debug("^%s is GMS package. Skipped...", package_name)
          continue
        else:
          logger.debug("!%s seems to be GMS but signature doesn't match!",
                       package_name)

      risk_score = self._compute_fair_permission_score(
          package["permissionsGranted"])
      # handle special permissions
      risk_score["special"] = 0
      for special_permission in package["permissionsSpecial"]:
        risk_score["special"] += RiskyPermissions.map_permission_to_score(
            special_permission)

      ungranted_score = self._compute_fair_permission_score(
          package["permissionsNotGranted"])

      is_related = False
      for score_type in risk_score:
        if risk_score[score_type]:
          logger.debug("+%s added %d points as %s pregranted score.",
                       package_name, risk_score[score_type], score_type)
          is_related = True
        total_granted_score += risk_score[score_type]
      for score_type in ungranted_score:
        if ungranted_score[score_type]:
          logger.debug("/%s added %d points as %s ungranted score.",
                       package_name, ungranted_score[score_type], score_type)
        total_ungranted_score += ungranted_score[score_type]

      if update_apps and is_related:
          self.related_apps.append(package)

    logger.debug("Total pregranted perms score: %2.2f", total_granted_score)
    logger.debug("Total ungranted perms score: %2.2f", total_ungranted_score)
    return total_granted_score

  def compute_score(self, hubble, normalize):
    return self._cambridge(hubble, normalize)


# NOTE TO IMPLEMENTORS:
# UNGRANTED_SCORE is calculated by running through compute_score on baseline
# builds with normalize=False. It represents the total score of permissions
# requested by all the preloaded apps but not pre-granted.
# This value is recorded but currently unused in any computation.
# Also note that BASE_SCORE and UNGRANTED_SCORE may change if the mappings
# and/or weightings for the permissions change.
class NRiskyPermissions(RiskyPermissions):
  API_LEVEL = 25
  BASE_SCORE = 307.5
  UNGRANTED_SCORE = 175


class ORiskyPermissions(RiskyPermissions):
  API_LEVEL = 27
  BASE_SCORE = 340
  UNGRANTED_SCORE = 167


class PRiskyPermissions(RiskyPermissions):
  API_LEVEL = 28
  BASE_SCORE = 342.5
  UNGRANTED_SCORE = 160


class QRiskyPermissions(RiskyPermissions):
  API_LEVEL = 29
  BASE_SCORE = 385
  UNGRANTED_SCORE = 190


class RRiskyPermissions(RiskyPermissions):
  API_LEVEL = 30
  BASE_SCORE = 462.5
  UNGRANTED_SCORE = 187.5


class SRiskyPermissions(RiskyPermissions):
  API_LEVEL = 31
  BASE_SCORE = 485
  UNGRANTED_SCORE = 175


class TRiskyPermissions(RiskyPermissions):
  API_LEVEL = 33
  BASE_SCORE = 500
  UNGRANTED_SCORE = 200


class CleartextTraffic(RiskAnalyzer):
  """Cleartext traffic risk analyzer.

  This class analyzes packages to see if they pose any security risk of using
  cleartext traffic in their communication.
  """
  PHI = 1
  LAMBDA_I = 0.25

  @staticmethod
  def get_scorer(api_level, logger, google_discount=False):
    if api_level == 33:
      return TCleartextTraffic(logger, google_discount)
    if api_level == 31:
      return SCleartextTraffic(logger, google_discount)
    if api_level == 30:
      return RCleartextTraffic(logger, google_discount)
    if api_level == 29:
      return QCleartextTraffic(logger, google_discount)
    elif api_level == 28:
      return PCleartextTraffic(logger, google_discount)
    elif api_level == 27 or api_level == 26:
      return OCleartextTraffic(logger, google_discount)
    elif api_level == 25 or api_level == 24:
      return NCleartextTraffic(logger, google_discount)
    else:
      raise Exception("API Level not handled!")

  def __init__(self, logger, google_discount):
    super().__init__()
    self.logger = logger
    self.google_discount = google_discount

  def compute_base_score(self, hubble, normalize):
    """Computes the base score of cleartext traffic risk.

    Args:
      hubble: A HubbleParser instance containing build to be evaluated.
      normalize: A boolean indicating whether or not to normalize the computed
                 base score against a baseline.

    Returns:
      An int representing the base or raw score of this metric.
    """
    logger = self.logger
    packages = hubble.packages
    baseline = BaselinePackages.get_instance(hubble.get_api_level())
    gms_packages = GMS.PACKAGES if self.google_discount else []
    total_score = 0
    update_apps = False
    if not self.related_apps and normalize:
      update_apps = True
    for package in packages:
      package_name = package["name"]

      if normalize:
        fuzzy_matched_package = PackageWhitelists.package_name_fuzzy_match(
            logger, package_name, baseline.get_all_packages())
        if fuzzy_matched_package:
          logger.debug("^%s skipped - fuzzy matched to GSI package %s",
                       package_name, fuzzy_matched_package)
          continue

      if package_name in gms_packages:
        if GMS.is_gms_package(package):
          logger.debug("^%s is GMS package. Skipping...", package_name)
          continue
        else:
          logger.warning("%s's signature does not match database. Careful!",
                         package_name)

      if not package["hasCode"]:
        logger.debug("^%s has no code. Skipping for now...", package_name)
        continue

      if package["usesCleartextTraffic"]:
        logger.debug("+%s uses cleartext traffic!", package_name)
        total_score += 1

        if "android.permission.INTERNET" not in package["permissionsGranted"]:
          logger.debug("-%s has no INTERNET permission? Discounting...",
                       package_name)
          total_score -= 1
        elif update_apps:
          self.related_apps.append(package)
    return total_score

  def compute_score(self, hubble, normalize):
    """Computes the CleartextTraffic risk score.

    Note that this method currently does NOT disregard platform signed apps.
    In fact, we should consider if we should double count those signed using
    platform apps to discourage this behavior among more privileged apps.
    We're using the Cambridge formula here to calculate the weighted risk score.

    Args:
      hubble: a valid HubbleParser object.
      normalize: a boolean indicating whether or not to normalize the score
                 with a baseline. This should be set to false when obtaining
                 baseline scores.

    Returns:
      A float representing the calculated risk score using the formula of
      probability x damage_potential.
    """
    return self._cambridge(hubble, normalize)


class NCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 15


class OCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 18


class PCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 12


class QCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 8


class RCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 8


class SCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 8


class TCleartextTraffic(CleartextTraffic):
  BASE_SCORE = 9


class HostileDownloader(RiskAnalyzer):
  """Analyzer assessing hostile downloader security risk.
  """
  PHI = 2.5
  LAMBDA_I = 0.25

  def __init__(self, logger, google_discount=True):
    super().__init__()
    self.logger = logger
    self.google_discount = google_discount
    self.installer_apps = []
    self.installer_apps_certs = set()

  @staticmethod
  def get_scorer(api_level, logger):
    pass

  def compute_base_score(self, hubble, normalize):
    logger = self.logger
    num_sources = 0
    num_platform_signed = 0

    packages = hubble.packages
    whitelist = PackageWhitelists.get_whitelist(hubble.get_oem())
    baseline = BaselinePackages.get_instance(hubble.get_api_level())
    gms_packages = GMS.PACKAGES if self.google_discount else []
    platform_signature = hubble.get_platform_signature()
    for package in packages:
      package_name = package["name"]

      if "android.permission.INSTALL_PACKAGES" not in package[
          "permissionsGranted"]:
        continue

      if package_name in whitelist.INSTALLER_PACKAGES:
        logger.debug("^%s is an official installer app. Skipping...",
                     package_name)
        continue

      if normalize:
        fuzzy_matched_package = PackageWhitelists.package_name_fuzzy_match(
            logger, package_name, baseline.get_all_packages())
        if fuzzy_matched_package:
          logger.debug("^%s skipping - fuzzy matched to GSI package %s",
                       package_name, fuzzy_matched_package)
          continue

      if package_name in gms_packages:
        if GMS.is_gms_package(package):
          logger.debug("^%s is GMS package. Skipping...", package_name)
          continue
        else:
          logger.warning("%s's signature does not match actual GMS package!",
                         package_name)

      self.installer_apps.append(package_name)
      signer_cert = package["certIds"][0]
      self.installer_apps_certs.add(signer_cert)
      logger.debug("+%s signed by %s can install other packages.", package_name,
                   signer_cert)
      if signer_cert == platform_signature:
        logger.warning("!%s's signer is platform key!", package_name)
        num_platform_signed += 1

    num_sources = len(self.installer_apps_certs)
    logger.debug("There are %d different sources of %d installer app",
                 num_sources, len(self.installer_apps))
    return num_sources + num_platform_signed

  def compute_score(self, hubble, normalize):
    logger = self.logger
    # we're using direct multiples of num_sources as iota here, to discourage
    # this phenomenon
    multiplier = 1
    base_score = self.compute_base_score(hubble, normalize)
    if not base_score:
      base_score = 1
    iota = multiplier * base_score
    if not normalize:
      iota = 1
    logger.debug("iota: %2.4f", iota)

    lambda_i = 0.25
    lambda_o = iota * lambda_i / (1 + lambda_i * (iota - 1))
    logger.debug("lambda_o: %2.4f", lambda_o)

    return lambda_o * self.PHI


def set_up_logging():
  logger = logging.getLogger(__name__)
  logger.setLevel(logging.ERROR)
  log_format = ("%(levelname)s:%(filename)s:%(funcName)s(%(lineno)d): "
                "%(message)s")
  s_handler = logging.StreamHandler()
  s_format = logging.Formatter(log_format)
  s_handler.setFormatter(s_format)
  logger.addHandler(s_handler)
  return logger
