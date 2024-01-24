//Copyright 2019 Uraniborg authors.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.uraniborg.hubble;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  final String TAG = "HUBBLE";

  // semantically tie the notion of app version to versionName, which we will update for every
  // major and minor release, instead of independently and separately update these values everytime.
  private final String VERSION = BuildConfig.VERSION_NAME;

  private HashMap<String, PackageMetadata> mAllPackages;
  private HashMap<String, byte[]> mAllCertificates;
  private HashMap<String, BinaryInfo> mAllBinaries;
  private HashMap<String, LibraryInfo> mAllLibraries;
  private HardwareInfo mHardwareInfo;
  private BuildInfo mBuildInfo;
  private PackageManager mPackageManager;
  private DevicePropertiesInfo mDeviceProps;
  private Executor mExecutor;

  private static String HEADER_FMT = "{ \"version\": \"%s\", \"%s\": %d,\n\"%s\": [\n";
  private static String FOOTER_STR = "\n]\n}";


  private boolean initialize() {
    final String tag = TAG + "-INIT";
    mAllPackages = new HashMap<>();
    mAllCertificates = new HashMap<>();
    mAllBinaries = new HashMap<>();
    mAllLibraries = new HashMap<>();
    mHardwareInfo = new HardwareInfo();
    mBuildInfo = new BuildInfo();
    mDeviceProps = new DevicePropertiesInfo();
    mExecutor = Executors.newSingleThreadExecutor();

    mPackageManager = getPackageManager();
    if (mPackageManager == null) {
      Log.e(tag, "Failed to obtain package manager");
      return false;
    }
    return true;
  }

  /*
   * other constants not available/visible from SDK
   */
  // this allows us to see installed packages in the 'disabled' or 'hidden' state.
  final int MATCH_HIDDEN_UNTIL_INSTALLED_COMPONENTS = 0x20000000;
  // this allows us to get APEX packages when calling getInstalledPackages
  final int MATCH_APEX = 0x40000000;

  private void getInstalledPackagesInformation() {
    String tag = TAG + "-PKGS";

    int flags = PackageManager.GET_ACTIVITIES |
                PackageManager.GET_GIDS |
                PackageManager.GET_INTENT_FILTERS |
                PackageManager.GET_META_DATA |
                PackageManager.GET_PERMISSIONS |
                PackageManager.GET_PROVIDERS |
                PackageManager.GET_RECEIVERS |
                PackageManager.GET_SERVICES |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                MATCH_APEX;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      flags |= PackageManager.GET_SIGNING_CERTIFICATES | MATCH_HIDDEN_UNTIL_INSTALLED_COMPONENTS;
    } else {
      flags |= PackageManager.GET_SIGNATURES;
    }

    @SuppressLint("WrongConstant") List<PackageInfo> installedPackagesAndApexes = mPackageManager.
        getInstalledPackages(flags);
    for (PackageInfo pkg : installedPackagesAndApexes) {
      PackageMetadata pkgMetadata = PackageMetadata.parse(this, pkg, mPackageManager);
      mAllPackages.put(pkg.packageName, pkgMetadata);
    }
    Log.d(tag, String.format("There are %d packages (including APEX)", mAllPackages.size()));
  }

  private void getAllCertificates() {
    final String tag = TAG + "-CERT";
    for (String pkgName : mAllPackages.keySet()) {
      PackageMetadata pkgMetadata = mAllPackages.get(pkgName);
      if (pkgMetadata == null) {
        Log.e(tag, String.format("Unexpected error getting pkg metadata for %s", pkgName));
        continue;
      }
      PackageInfo pkgInfo = pkgMetadata.ref;
      Signature[] signatures;
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        signatures = pkgInfo.signatures;
      } else {
        SigningInfo signingInfo = pkgInfo.signingInfo;
        if (signingInfo.hasMultipleSigners()) {
          signatures = signingInfo.getApkContentsSigners();
        } else {
          signatures = signingInfo.getSigningCertificateHistory();
        }
      }
      if (signatures == null) {
        Log.e(tag, String.format("Failed to grab signature for package: %s", pkgName));
        continue;
      }

      JSONArray signaturesJSONArray = new JSONArray();
      for (Signature signature : signatures) {
        String encodedSignatureDigest = Utilities.computeSHA256DigestOfCertificate(signature);
        if (encodedSignatureDigest == null) {
          Log.e(tag, String.format("Failed to compute hash for cert of package: %s", pkgName));
          continue;
        }
        if (!mAllCertificates.containsKey(encodedSignatureDigest)) {
          mAllCertificates.put(encodedSignatureDigest, signature.toByteArray());
        }
        signaturesJSONArray.put(encodedSignatureDigest);
      }
      pkgMetadata.certIds = signaturesJSONArray;
    }
  }

  private void getAllBinaries() {
    final String tag = TAG + "-BININFO";

    // first get the system path
    String binPaths = System.getenv("PATH");
    if (binPaths == null) {
      Log.e(tag, String.format("Error getting system PATH environment value."));
      return;
    }
    Log.d(tag, String.format("binPaths: %s", binPaths));

    String[] paths = binPaths.split(":");
    List<File> accessibleBins = new ArrayList<>();
    for (String path : paths) {
      accessibleBins.addAll(Utilities.getAllFilesInDirectory(path, false));
    }

    for (File binFile : accessibleBins) {
      BinaryInfo binaryInfo = new BinaryInfo();
      binaryInfo.name = binFile.getName();
      binaryInfo.installPath = binFile.getParent();
      binaryInfo.hash = Utilities.computeSHA256DigestOfFile(this, binFile.getAbsolutePath());
      binaryInfo.fileSizeInBytes = binFile.length();

      if (binaryInfo.hash != null ) {
        mAllBinaries.put(binaryInfo.hash, binaryInfo);
      }
    }
    Log.d(tag, String.format("There are %d accessible binaries.", mAllBinaries.size()));
  }

  private void getAllLibraries() {
    final String tag = TAG + "-LIBINFO";

    for (String libDir : LibraryInfo.LIB_PATHS) {
      for (File libFile : Utilities.getAllFilesInDirectory(libDir, false)) {
        LibraryInfo libraryInfo = new LibraryInfo();
        libraryInfo.name = libFile.getName();
        libraryInfo.installPath = libFile.getParent();

        if (libraryInfo.installPath.contains("64")) {
          libraryInfo.bits = 64;
        } else {
          libraryInfo.bits = 32;
        }

        libraryInfo.hash = Utilities.computeSHA256DigestOfFile(this, libFile.getAbsolutePath());

        if (libraryInfo.hash != null) {
          mAllLibraries.put(libraryInfo.hash, libraryInfo);
        }

        libraryInfo.fileSizeInBytes = libFile.length();
      }
    }

    Log.d(tag, String.format("There are %d libraries found.", mAllLibraries.size()));

  }

  private void getHardwareInformation() {
    final String tag = TAG + "-HWINFO";
    mHardwareInfo.brand = Build.BRAND;
    mHardwareInfo.boardName = Build.BOARD;
    mHardwareInfo.deviceName = Build.DEVICE;
    mHardwareInfo.oem = Build.MANUFACTURER;
    mHardwareInfo.modelName = Build.MODEL;
    mHardwareInfo.productName = Build.PRODUCT;
    mHardwareInfo.hardwareName = Build.HARDWARE;
    mHardwareInfo.hash = mHardwareInfo.computeHash();

    //getSystemService(Context.PERSISTENT_DATA_BLOCK_SERVICE);
  }

  @Nullable
  private String getKernelVersion() {
    // there are many ways to get the kernel version, but not all can succeed on every build
    String cmd = "uname -a";
    ExecutionResult result = Utilities.executeInShell(cmd);
    if (!result.exceptionTriggered && result.exitCode == 0 && !result.stdOutStr.trim().isEmpty()) {
      return result.stdOutStr.trim();
    }

    // if we reach here, means the first method didn't give us satisfactory answers.
    cmd = "cat /proc/version";
    result = Utilities.executeInShell(cmd);
    if (!result.exceptionTriggered && result.exitCode == 0 && !result.stdOutStr.trim().isEmpty()) {
      return result.stdOutStr.trim();
    }

    // if we reach here, we're out of ideas!! :(
    return null;
  }

  private void getBuildInformation() {
    final String tag = TAG + "-BUILDINFO";
    mBuildInfo.apiLevel = Build.VERSION.SDK_INT;
    mBuildInfo.fingerprint = Build.FINGERPRINT;
    mBuildInfo.securityPatchLevel = Build.VERSION.SECURITY_PATCH;
    mBuildInfo.bootloaderVersion = Build.BOOTLOADER;
    mBuildInfo.radioVersion = Build.getRadioVersion();
    mBuildInfo.locale = Locale.getDefault().getDisplayName();

    mBuildInfo.kernelVersion = getKernelVersion();
    if (mBuildInfo.kernelVersion == null || mBuildInfo.kernelVersion.isEmpty()) {
      mBuildInfo.kernelVersion = Build.UNKNOWN;
    }
  }

  private void getDeviceProperties() {
    final String tag = TAG + "-GETPROP";
    String cmd = "getprop";
    ExecutionResult result = Utilities.executeInShell(cmd);
    if (!result.exceptionTriggered && result.exitCode != null && result.exitCode.intValue() == 0 &&
        result.stdOutStr != null) {
      mDeviceProps.encodedDevProps = Base64.encodeToString(result.stdOutStr.getBytes(),
          Base64.NO_WRAP);
    }
  }


  private void writePackagesToFile() {
    final String tag = TAG + "-W_PKG";
    final String PKG_FILENAME = "packages.txt";

    String header = String.format(HEADER_FMT, VERSION, "totalPackages", mAllPackages.size(),
        "packages");
    Utilities.writeToFile(this, PKG_FILENAME, header, false);
    int i = 0;
    String[] skip = new String[] {"ref"};
    for (PackageMetadata pkgMetadata : mAllPackages.values()) {
      Utilities.writeToFile(this, PKG_FILENAME, pkgMetadata.getJSONString(skip), true);
      if (i == mAllPackages.size() - 1) {
        continue;
      }
      Utilities.writeToFile(this, PKG_FILENAME, ",\n", true);
      i++;
    }
    Utilities.writeToFile(this, PKG_FILENAME, FOOTER_STR, true);
  }

  private void writeCertsToFile() {
    final String tag = TAG + "-W_CRT";
    final String CERT_FILENAME = "certificates.txt";

    String header = String.format(HEADER_FMT, VERSION, "totalCerts", mAllCertificates.size(),
        "certs");
    Utilities.writeToFile(this, CERT_FILENAME, header, false);
    int i = 0;
    for (String certHash : mAllCertificates.keySet()) {
      JSONObject cert = new JSONObject();
      try {
        cert.put("hash", certHash);
        cert.put("encodedCert", Base64.encodeToString(mAllCertificates.get(certHash),
            Base64.NO_WRAP));
        Utilities.writeToFile(this, CERT_FILENAME, cert.toString(2), true);
      } catch (JSONException e) {
        Log.e(tag, String.format("Facing errors dealing with JSON: %s", e.getMessage()));
      }
      if (i == mAllCertificates.size() - 1) {
        continue;
      }
      Utilities.writeToFile(this, CERT_FILENAME, ",\n", true);
      i++;

    }
    Utilities.writeToFile(this, CERT_FILENAME, FOOTER_STR, true);
  }

  private void writeBinsToFile() {
    final String tag = TAG + "-W_BIN";
    final String BIN_FILENAME = "binaries.txt";

    String header = String.format(HEADER_FMT, this.VERSION, "totalBins", mAllBinaries.size(),
        "bins");
    Utilities.writeToFile(this, BIN_FILENAME, header, false);
    int i = 0;
    for (String binHash : mAllBinaries.keySet()) {
      BinaryInfo binInfo = mAllBinaries.get(binHash);
      Utilities.writeToFile(this, BIN_FILENAME, binInfo.getJSONString(null), true);

      if (i == mAllBinaries.size() - 1) {
        continue;
      }
      Utilities.writeToFile(this, BIN_FILENAME, ",\n", true);
      i++;
    }
    Utilities.writeToFile(this, BIN_FILENAME, FOOTER_STR, true);
  }

  private void writeLibsToFile() {
    final String tag = TAG + "-W_LIB";
    final String LIB_FILENAME = "libraries.txt";

    String header = String.format(HEADER_FMT, VERSION, "totalLibs", mAllLibraries.size(), "libs");
    Utilities.writeToFile(this, LIB_FILENAME, header, false);
    int i = 0;
    for (String libHash : mAllLibraries.keySet()) {
      LibraryInfo libInfo = mAllLibraries.get(libHash);
      Utilities.writeToFile(this, LIB_FILENAME, libInfo.getJSONString(null), true);

      if (i == mAllLibraries.size() - 1) {
        continue;
      }
      Utilities.writeToFile(this, LIB_FILENAME, ",\n", true);
      i++;
    }
    Utilities.writeToFile(this, LIB_FILENAME, FOOTER_STR, true);
  }

  private void writeHardwareToFile() {
    final String tag = TAG + "-W_HW";
    final String HW_FILENAME = "hardware.txt";

    writeSingleInfoToFile(HW_FILENAME, "totalHardware", "hwInfo",
        mHardwareInfo, null);
  }

  private void writeBuildToFile() {
    final String tag = TAG + "-W_BI";
    final String BUILD_FILENAME = "build.txt";

    writeSingleInfoToFile(BUILD_FILENAME, "totalBuild", "buildInfo",
        mBuildInfo, null);
  }

  private void writeDevicePropsToFile() {
    final String tag = TAG + "-W-DP";
    final String DP_FILENAME = "device_properties.txt";

    writeSingleInfoToFile(DP_FILENAME, "totalDeviceProps", "b64EncodedDeviceProps",
        mDeviceProps, null);
  }

  private void writeSingleInfoToFile(@NotNull String filename, @NotNull String countName,
                                     @NotNull String fieldName, @NotNull BaseInfo info,
                                     @Nullable String[] skip) {
    StringBuilder sbToWrite = new StringBuilder(String.format(HEADER_FMT, VERSION, countName, 1,
        fieldName));
    sbToWrite.append(info.getJSONString(skip));
    sbToWrite.append(FOOTER_STR);
    Utilities.writeToFile(this, filename, sbToWrite.toString(), false);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView textView = findViewById(R.id.textView);
    textView.setText(R.string.scan_start);

    initialize();

    mExecutor.execute(() -> {
      final long start = SystemClock.elapsedRealtime();

      getInstalledPackagesInformation();
      getAllCertificates();
      getAllBinaries();
      getAllLibraries();
      getHardwareInformation();
      getBuildInformation();
      getDeviceProperties();

      final long duration = SystemClock.elapsedRealtime() - start;

      writePackagesToFile();
      writeCertsToFile();
      writeBinsToFile();
      writeLibsToFile();
      writeHardwareToFile();
      writeBuildToFile();
      writeDevicePropsToFile();

      Log.d(TAG, String.format("Execution took %d ms.", duration));
      Log.w(TAG, String.format("Build version: %d", Build.VERSION.SDK_INT));
      Log.w(TAG,
              String.format("Results are available at: %s", Utilities.getResultStorageDirectory(this)));
      runOnUiThread(() -> textView.setText(getResources().getString(R.string.scan_end)));
    });
  }
}
