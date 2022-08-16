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

"""Package Whitelists Module.

This module defines a generic class that encapsulates the necessary attributes
of what package whitelists would be like. Package whitelists and baseline may
differ according to OEM and API level - thus the need for specificity.

Currently, we are exempting safety information packages.

Google Mobile Service (GMS) packages are also exempted.
"""

import json
import os


class BaselinePackages:
  """A set of packages used as point of reference based on API level.

  The package names are observed from either AOSP or GSI builds.
  """

  data_filename = ""
  instance = None
  instances = {}
  api_level_to_filename = {
      24: "data/N-AOSP.json",
      25: "data/N-AOSP.json",
      26: "data/O-AOSP.json",
      27: "data/O-AOSP.json",
      28: "data/P-AOSP.json",
      29: "data/Q-GSI.json",
      30: "data/R-GSI.json",
      31: "data/S-GSI.json",
      33: "data/T-GSI.json",  # per https://developer.android.com/reference/android/os/Build.VERSION_CODES#TIRAMISU
  }

  @staticmethod
  def get_instance(api_level):
    """Obtains a usable instance of BaselinePackages according to API Level.

    Args:
      api_level: An int representing the api level.

    Returns:
      An instance of BaselinePackages that is tailored for the API level.
    """
    instance = BaselinePackages.instances.get(api_level)
    if not instance:
      instance = BaselinePackages()
      curr_dir = os.path.dirname(os.path.abspath(__file__))
      data_filepath = os.path.join(curr_dir,
                                   BaselinePackages.api_level_to_filename.get(
                                       api_level))
      instance.load_data_files(data_filepath)
      BaselinePackages.instances[api_level] = instance
    return instance

  def load_data_files(self, data_filepath):
    """Reads in data files & converts JSON objects into instance attributes.

    Args:
      data_filepath: the filepath of the data file that is to be read in.
    """
    in_json = ""
    with open(data_filepath, "r") as f_in:
      in_json = f_in.read()
    in_json = json.loads(in_json)
    self.packages_all = in_json["packagesAll"]
    self.packages_no_code = in_json["packagesNoCode"]
    self.packages_platform_signed = in_json["platformAppsAll"]
    self.packages_platform_no_code = in_json["platformAppsNoCode"]
    self.packages_shared_uid = in_json["packagesSharedUid"]

  def get_all_packages(self, no_code=False):
    if no_code:
      return self.packages_no_code
    return self.packages_all

  def get_platform_signed_packages(self, no_code=False):
    if no_code:
      return self.packages_platform_no_code
    return self.packages_platform_signed

  def get_shared_uid_packages(self):
    return self.packages_shared_uid


class GMS:
  """A class defining Google Mobile Services (GMS) apps.

  Maintenance: This class may need to be updatede in the future should the
               signing certificates be rotated/changed.
  """
  PACKAGES = {
      # The actual gmscore APK
      "com.google.android.gms":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Drive
      "com.google.android.apps.docs":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Youtube
      "com.google.android.youtube":
          "3d7a1223019aa39d9ea0e3436ab7c0896bfb4fb679f4de5fe7c23f326c8f994a",
      # Google Play Client
      "com.android.vending":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Chrome browser
      "com.android.chrome":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Photos
      "com.google.android.apps.photos":
          "3d7a1223019aa39d9ea0e3436ab7c0896bfb4fb679f4de5fe7c23f326c8f994a",
      # Google Maps
      "com.google.android.apps.maps":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Gmail
      "com.google.android.gm":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Services Framework
      "com.google.android.gsf":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google
      "com.google.android.googlequicksearchbox":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Setup Wizard
      "com.google.android.setupwizard":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Calendar
      "com.google.android.calendar":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
      # Google Movies
      "com.google.android.videos":
          "3d7a1223019aa39d9ea0e3436ab7c0896bfb4fb679f4de5fe7c23f326c8f994a",
  }

  @staticmethod
  def is_gms_package(package):
    """Checks if a package is a legitimate GMS APK based on signer cert.

    Args:
      package: A JSON object representing info about an APK.

    Returns:
      True if the package's signature matches the correct one.
    """
    package_name = package["name"]
    if package_name not in GMS.PACKAGES:
      return False

    package_signature = package["certIds"][0]
    if package_signature == GMS.PACKAGES.get(package_name):
      return True
    return False


class PackageWhitelists:
  """The main class.

  This class defines globally excluded packages. It can be inherited to create
  OEM-specific excluded packages.

  As of now, we found 3 categories of what we think can legitimately be
  excluded:
  1. The "android" APK, which every Android device must have.
  2. Regulatory APK that displays safety or other legal information.
  3. Installer APKs in the form of 1P OEM app store. Note that 3P installers
      should never be included in the exception list, and are considered as
      adding risk to the build!

  Categories 1 & 2 are listed using EXCLUDED_PACKAGES set, and the 3rd is listed
  using INSTALLER_PACKAGES dict.

  More categories may be added as necessary in the future.
  """

  PREFIXES = {
      "com.android.",
      "android."
  }

  EXCLUDED_PACKAGES = {
      # These are globally excluded packages. In OEM-specific instances,
      # regulatory safety/legal APKs can be included here.
      "android",  # the framework package
      "com.uraniborg.hubble",   # our observer app
  }

  INSTALLER_PACKAGES = {
      # The other exception we are making is for 1P app stores. We are listing
      # Google Play Store client as the global whitelist as it is a required app
      # in MADA.
      "com.android.vending":
          "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
  }

  @staticmethod
  def get_whitelist(oem):
    """Gets an oem-specific whitelist.

    Args:
      oem: A string describing the manufacturer.

    Returns:
      A specific PackageWhitelists instance that contains OEM-specific excluded
      packages.
    """
    if oem == "Google":
      return Google()
    elif oem == "samsung":
      return Samsung()
    elif oem == "HUAWEI":
      return Huawei()
    elif oem == "HMD Global":
      return Nokia()
    elif oem == "Sony":
      return Sony()
    elif oem == "motorola":
      return Motorola()
    elif oem == "asus":
      return Asus()
    elif oem == "OnePlus":
      return OnePlus()
    else:
      return PackageWhitelists()

  @staticmethod
  def package_name_fuzzy_match(logger, package_name, whitelist):
    """Performs package name fuzzy matching.

    We are doing some sort of prefix matching currently.

    Args:
      logger: a logger object
      package_name: the name of the package to be matched
      whitelist: a list containing package names to be matched with.

    Returns:
      The corresponding package name within the whitelist that the package_name
      was matched to. In case of no match, <code>None</code> is returned.
    """
    if package_name in whitelist:
      logger.debug("%s is direct match!", package_name)
      return package_name

    for whitelisted_package in whitelist:
      for prefix in PackageWhitelists.PREFIXES:
        truncated_package_name = ""
        if whitelisted_package.startswith(prefix):
          truncated_package_name = "." + whitelisted_package[len(prefix):]

          if package_name.endswith(truncated_package_name):
            return whitelisted_package

    return None


class Samsung(PackageWhitelists):
  EXCLUDED_PACKAGES = PackageWhitelists.EXCLUDED_PACKAGES | {
      # Safety information
      "com.samsung.safetyinformation",
    }

  INSTALLER_PACKAGES = {
      **PackageWhitelists.INSTALLER_PACKAGES,
      # Galaxy Store with signer
      "com.sec.android.app.samsungapps":
          "fba3af4e7757d9016e953fb3ee4671ca2bd9af725f9a53d52ed4a38eaaa08901",
  }


class Google(PackageWhitelists):
  EXCLUDED_PACKAGES = PackageWhitelists.EXCLUDED_PACKAGES | {
      "com.android.safetyregulatoryinfo",
  }


class Nokia(PackageWhitelists):
  EXCLUDED_PACKAGES = PackageWhitelists.EXCLUDED_PACKAGES | {
      "com.hmdglobal.app.legalinformation",
  }


class Huawei(PackageWhitelists):
  INSTALLER_PACKAGES = {
      **PackageWhitelists.INSTALLER_PACKAGES,
      # AppGallery (Huawei's App Store)
      "com.huawei.appmarket":
          "ffe391e0ea186d0734ed601e4e70e3224b7309d48e2075bac46d8c667eae7212",
  }


class Sony(PackageWhitelists):
  pass


class Motorola(PackageWhitelists):
  pass


class Asus(PackageWhitelists):
  pass


class OnePlus(PackageWhitelists):
  pass


# NOTE: Create new oem-specific classes here:
