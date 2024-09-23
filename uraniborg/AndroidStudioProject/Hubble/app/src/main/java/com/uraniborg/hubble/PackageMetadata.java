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

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.PatternMatcher;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PackageMetadata extends BaseInfo {
  protected String hash = null;
  protected String name = null;
  protected String label = null;
  protected CharSequence description = null;
  protected int versionCode;
  protected String versionName;
  protected JSONArray certIds;
  protected boolean isEnabled = false;
  protected boolean isTestOnly = false;
  protected boolean isFactoryTest = false;
  protected boolean isSuspended = false;
  protected boolean isApex = false;
  protected boolean isPreinstalled = false;
  protected boolean isHidden = false;
  protected boolean hasCode = true;
  protected boolean usesCleartextTraffic = true;
  protected String installLocation = null;
  protected JSONArray permissionsDeclared = null;
  protected JSONArray permissionsNotGranted = null;
  protected JSONArray permissionsGranted = null;
  protected Set<String> permissionsSpecial = new HashSet<>();
  protected JSONArray activities = null;
  protected JSONArray services = null;
  protected JSONArray receivers = null;
  protected JSONArray providers = null;
  protected PackageInfo ref = null;
  protected long firstInstallTime;
  protected String sharedUserId;
  protected int sharedUserLabel;
  protected String[] splitNames;
  protected int[] kernelGids;
  protected long fileSizeInBytes;

  private static final Pattern PERMISSION_PATTERN = Pattern.compile("[a-z\\.]+BIND_[\\w]+");


  /**
   * @param context The context to operate in.
   * @param packageInfo A valid {@link PackageInfo} object containing information about an APK.
   * @param packageManager A valid {@link PackageManager} object used to load app description.
   * @return a valid {@link PackageMetadata} object containing parsed information about the APK.
   */
  static public PackageMetadata parse(Context context, @NotNull PackageInfo packageInfo,
                                      @NotNull PackageManager packageManager) {
    PackageMetadata result = new PackageMetadata();
    result.ref = packageInfo;
    result.name = packageInfo.packageName;

    if (packageInfo.applicationInfo != null) {
      ApplicationInfo appInfo = packageInfo.applicationInfo;
      result.installLocation = appInfo.sourceDir;
      result.hash = Utilities.computeSHA256DigestOfFile(context, appInfo.sourceDir);
      result.label = context.getPackageManager().getApplicationLabel(appInfo).toString();
      result.isEnabled = appInfo.enabled;

      result.hasCode = ((appInfo.flags & ApplicationInfo.FLAG_HAS_CODE) != 0);
      result.isTestOnly = ((appInfo.flags & ApplicationInfo.FLAG_TEST_ONLY) != 0);
      result.isFactoryTest = ((appInfo.flags & ApplicationInfo.FLAG_FACTORY_TEST) != 0);
      result.isSuspended = ((appInfo.flags & ApplicationInfo.FLAG_SUSPENDED) != 0);

      result.isPreinstalled = (((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ||
                                ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0));

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        result.usesCleartextTraffic =
            (appInfo.flags & ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0;
      }

      result.isHidden = (appInfo.flags & ApplicationInfo.FLAG_INSTALLED) == 0;
      result.description = appInfo.loadDescription(packageManager);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      result.isApex = packageInfo.isApex;
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
      result.versionCode = packageInfo.versionCode;
    } else {
      // versionCode and versionCodeMajor is combined together as a single long value. To extract
      // only the versionCode, we need to get the lower 32 bits.
      result.versionCode = (int) packageInfo.getLongVersionCode() & 0xFFFFFFFF;
    }
    result.versionName = packageInfo.versionName;
    result.firstInstallTime = packageInfo.firstInstallTime;
    result.sharedUserId = packageInfo.sharedUserId;
    result.sharedUserLabel = packageInfo.sharedUserLabel;
    result.splitNames = packageInfo.splitNames;
    result.kernelGids = packageInfo.gids;

    // compute package size
    result.fileSizeInBytes = new File(result.installLocation).length();

    // deal with permissions
    result.parseDeclaredPermissions();
    result.parseRequestedPermissions();

    // deal with components
    result.parseServices(packageManager);
    result.parseActivityInfo(packageManager, false);
    result.parseActivityInfo(packageManager, true);
    result.parseProviders(packageManager);

    return result;
  }

  @Nullable
  private String getProtectionLevelString(int protectionLevel) {
    final String TAG = "getProtLevelStr";
    try {
      Method method = PermissionInfo.class.getMethod("protectionToString", int.class);
      String result = (String) method.invoke(null, protectionLevel);
      return result;

    } catch (NoSuchMethodException|SecurityException e) {
      Log.e(TAG, String.format("Failed to get 'protectionToString' method from PermissionInfo: " +
          "%s", e.getMessage()));
    } catch (IllegalAccessException|InvocationTargetException e) {
      Log.e(TAG, String.format("Failed to invoke method 'protectionToString': %s", e.getMessage()));
    }
    return null;
  }

  public boolean parseServices(PackageManager packageManager) {
    final String TAG = "parseServices";
    ServiceInfo[] services = ref.services;
    this.services = new JSONArray();
    if (services == null) {
      return true;
    }

    for (ServiceInfo service : services) {
      JSONObject serviceJson = parseComponentInfo(service, packageManager);
      if (serviceJson == null) {
        Log.e(TAG, String.format("Failed to parse %s", service.name));
        continue;
      }

      // parse permission
      String permName = service.permission;
      if (permName != null && PERMISSION_PATTERN.matcher(permName).matches()) {
        this.permissionsSpecial.add(permName);
      }
      try {
        serviceJson.put("permission", (service.permission == null) ? JSONObject.NULL : service.permission);
      } catch (JSONException e) {
        Log.e(TAG, String.format("Failed to put value to JSON Object: %s", e.getMessage()));
        return false;
      }
      this.services.put(serviceJson);
    }
    return true;
  }

  /**
   * A method that parses {@link ActivityInfo} objects. This method is useful to parse both
   * activities and receivers
   * @param packageManager The system's instance of {@link PackageManager}.
   * @param parseReceivers If this is set to <code>true</code>, this method will parse the APK's
   *                      receivers. If this is set to <code>false</code>, it will then parse the
   *                      activities instead.
   * @return a boolean indicating the success of the overall operation.
   */
  public boolean parseActivityInfo(PackageManager packageManager, boolean parseReceivers) {
    final String TAG = "parseActivityInfo";
    ActivityInfo[] activityInfos;
    if (parseReceivers) {
      this.receivers = new JSONArray();
      activityInfos = ref.receivers;
    } else {
      this.activities = new JSONArray();
      activityInfos = ref.activities;
    }

    if (activityInfos == null) {
      return true;
    }

    for (ActivityInfo activityInfo : activityInfos) {
      JSONObject resultJson = parseComponentInfo(activityInfo, packageManager);
      if (resultJson == null) {
        Log.e(TAG, String.format("Failed to parse %s", activityInfo.name));
        continue;
      }

      // parse permission
      String permName = activityInfo.permission;
      if (permName != null && PERMISSION_PATTERN.matcher(permName).matches()) {
        this.permissionsSpecial.add(permName);
      }
      try {
        resultJson.put("permission", (activityInfo.permission == null) ? JSONObject.NULL :
            activityInfo.permission);
      } catch (JSONException e) {
        Log.e(TAG, String.format("Failed to put value to JSON Object: %s", e.getMessage()));
        return false;
      }

      if (parseReceivers) {
        this.receivers.put(resultJson);
      } else {
        this.activities.put(resultJson);
      }
    }
    return true;
  }

  public boolean parseProviders(PackageManager packageManager) {
    final String TAG = "parseProviders";
    this.providers = new JSONArray();
    ProviderInfo[] providerInfos = ref.providers;
    if (providerInfos == null) {
      return true;
    }

    for (ProviderInfo provider : providerInfos) {
      JSONObject providerJson = parseComponentInfo(provider, packageManager);
      if (providerJson == null) {
        Log.e(TAG, String.format("Failed to parse %s", provider.name));
        continue;
      }

      JSONArray pathPermsJson = new JSONArray();
      if (provider.pathPermissions != null) {
        for (PathPermission pp : provider.pathPermissions) {
          JSONObject ppJson = parsePathPermission(pp);
          if (ppJson == null) {
            Log.e(TAG, String.format("Failed to parse PathPermission for provider: %s",
                provider.name));
            continue;
          }
          pathPermsJson.put(ppJson);
        }
      }

      JSONArray uriPermPatternsJson = new JSONArray();
      if (provider.uriPermissionPatterns != null) {
        for (PatternMatcher pp : provider.uriPermissionPatterns) {
          JSONObject ppJson = parsePatternMatcher(pp);
          if (ppJson == null) {
            Log.e(TAG, String.format("Failed to parse uriPermissionPatterns for %s",
                provider.name));
            continue;
          }
          uriPermPatternsJson.put(ppJson);
        }
      }

      try {
        providerJson.put("authority", (provider.authority != null) ? provider.authority :
            JSONObject.NULL);
        providerJson.put("grantUriPermissions", provider.grantUriPermissions);


        providerJson.put("permissionRead", (provider.readPermission != null) ?
            provider.readPermission : JSONObject.NULL);
        providerJson.put("permissionWrite", (provider.writePermission != null) ?
            provider.writePermission : JSONObject.NULL);

        providerJson.put("pathPermissions", pathPermsJson);
        providerJson.put("uriPermissionPatterns", uriPermPatternsJson);

        // API Level specific fields
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          providerJson.put("forceUriPermissions", provider.forceUriPermissions);
        } else {
          providerJson.put("forceUriPermissions", JSONObject.NULL);
        }

      } catch (JSONException e) {
        Log.e(TAG, String.format("Failed to put value into JSON Object: %s", e.getMessage()));
        return false;
      }
      this.providers.put(providerJson);
    }

    return true;
  }


  private JSONObject parsePathPermission(PathPermission pp) {
    final String TAG = "parsePP";
    JSONObject result = parsePatternMatcher(pp);
    if (result == null) {
      Log.e(TAG, String.format("Failed to parse pp: %s", pp.toString()));
      return null;
    }
    String permissionR = pp.getReadPermission();
    String permissionW = pp.getWritePermission();
    try {
      result.put("permissionRead", (permissionR != null) ? permissionR : JSONObject.NULL);
      result.put("permissionWrite", (permissionW != null) ? permissionW : JSONObject.NULL);
    } catch (JSONException e) {
      Log.e(TAG, String.format("Failed to put value into JSON Object: %s", e.getMessage()));
      return null;
    }
    return result;
  }

  /**
   * Helper function to parse {@link PatternMatcher} objects. This method extracts
   * {@link PatternMatcher#getPath()} and {@link PatternMatcher#getType()} information.
   * @param patternMatcher the {@link PatternMatcher} object to be parsed.
   * @return a JSONObject representing the parsed information. <code>null</code> is returned if
   * there is any errors.
   */
  @Nullable
  private JSONObject parsePatternMatcher(@NotNull PatternMatcher patternMatcher) {
    final String TAG = "parsePM";
    JSONObject result = new JSONObject();
    String path = patternMatcher.getPath();

    String type = null;
    switch (patternMatcher.getType()) {
      case PatternMatcher.PATTERN_LITERAL:
        type = "LITERAL";
        break;
      case PatternMatcher.PATTERN_ADVANCED_GLOB:
        type = "ADVANCED_GLOB";
        break;
      case PatternMatcher.PATTERN_PREFIX:
        type = "PREFIX";
        break;
      case PatternMatcher.PATTERN_SIMPLE_GLOB:
        type = "GLOB";
        break;
    }

    try {
      result.put("path", (path != null) ? path : JSONObject.NULL);
      result.put("type", (type != null) ? type : JSONObject.NULL);

    } catch (JSONException e) {
      Log.e(TAG, String.format("Failed to parse %s with error: %s", patternMatcher.toString(),
          e.getMessage()));
      return null;
    }

    return result;
  }

  /**
   * Helper function that handles the parsing of a generic {@link ComponentInfo}. Fields that are
   * parsed are: {@link ComponentInfo#name}, {@link ComponentInfo#enabled},
   * {@link ComponentInfo#exported}, {@link ComponentInfo#labelRes},
   * {@link ComponentInfo#nonLocalizedLabel}, {@link ComponentInfo#descriptionRes}.
   * @param info A {@link ComponentInfo} instance to be parsed.
   * @param packageManager The system's {@link PackageManager} instance.
   * @return a {@link JSONObject} encapsulating parsed fields, including name, enabled, exported,
   * label, nonLocalizedLabel, and description. <code>null</code> is returned indicating error.
   */
  @Nullable
  private JSONObject parseComponentInfo(@NotNull ComponentInfo info, @NotNull PackageManager packageManager) {
    final String TAG = "parseCompInfo";
    JSONObject resultJson = new JSONObject();
    try {
      resultJson.put("name", info.name);
      resultJson.put("isEnabled", info.enabled);
      resultJson.put("isExported", info.exported);
      // passing in an ApplicationInfo obj is more robust than using the package name.
      Resources resources = packageManager.getResourcesForApplication(info.applicationInfo);

      // extract label information
      JSONArray labels = new JSONArray();
      try {
        if (info.labelRes != 0) {
          // doing this in a try statement as apparently non-zero resource ID could still fail.
          labels.put(resources.getString(info.labelRes));
        }
      } catch (Resources.NotFoundException e) {
        Log.e(TAG, String.format("Failed to get string with resource id: %d: %s", info.labelRes,
            e.getMessage()));
      }
      if (info.nonLocalizedLabel != null) {
        labels.put(info.nonLocalizedLabel);
      }
      resultJson.put("labels", labels);

      // extract description of info
      if (info.descriptionRes != 0) {
        try {
          // doing the same as labelRes (defensive coding)
          resultJson.put("desc", resources.getString(info.descriptionRes));
        } catch (Resources.NotFoundException e) {
          Log.e(TAG, String.format("Failed to get desc. string from resource id: %d: %s",
              info.descriptionRes, e.getMessage()));
          resultJson.put("desc", JSONObject.NULL);
        }
      } else {
        resultJson.put("desc", JSONObject.NULL);
      }

    } catch (JSONException e) {
      Log.e(TAG, String.format("Failed to put item into JSONObject: %s", e.getMessage()));
      return null;
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, String.format("Failed to get resources for package %s: %s", info.packageName,
          e.getMessage()));
      return null;
    }
    return resultJson;
  }

  /**
   * Parses the permissions requested by the APK. This method will initialize & populate either the
   * {@link PackageMetadata#permissionsGranted} or {@link PackageMetadata#permissionsNotGranted}
   * fields.
   */
  public void parseRequestedPermissions() {
    final String TAG = "getRequestedPerms";
    String[] perms = ref.requestedPermissions;
    int[] grantedFlags = ref.requestedPermissionsFlags;
    this.permissionsNotGranted = new JSONArray();
    this.permissionsGranted = new JSONArray();

    if (perms == null) {
      return;
    }

    // to ensure order, we're enumerating by index
    for (int i = 0; i < perms.length; i++) {
      if ((grantedFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
        this.permissionsGranted.put(perms[i]);
      } else {
        this.permissionsNotGranted.put(perms[i]);
      }
    }
  }

  /**
   * Parses the custom declared permission in this APK. This method will initialize & populate the
   * {@link PackageMetadata#permissionsDeclared} field.
   * @return <code>true</code> if no errors were encountered. <code>false</code> otherwise.
   */
  public boolean parseDeclaredPermissions() {
    final String TAG = "getDeclaredPerms";
    PermissionInfo[] perms = ref.permissions;
    //JSONArray result = new JSONArray();
    this.permissionsDeclared = new JSONArray();
    boolean isSuccessful = true;

    if (perms == null) {
      return isSuccessful;
    }


    for (PermissionInfo perm : perms) {
      JSONObject permJson = new JSONObject();

      String protectionLevel = null;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        int basePermissionType = perm.getProtection();
        int protLevelFlags = perm.getProtectionFlags();
        protectionLevel = getProtectionLevelString(basePermissionType | protLevelFlags);

      } else {
        int protLevel = perm.protectionLevel;
        protectionLevel = getProtectionLevelString(protLevel);
      }
      protectionLevel = (protectionLevel == null) ? "" : protectionLevel;

      try {
        permJson.put("name", perm.name);
        permJson.put("protLevel", protectionLevel);
      } catch (JSONException e) {
        Log.e(TAG, String.format("Failed to insert object into JSON Object: %s", e.getMessage()));
        isSuccessful = false;
        continue;
      }

      permissionsDeclared.put(permJson);
    }
    return isSuccessful;
  }

}
