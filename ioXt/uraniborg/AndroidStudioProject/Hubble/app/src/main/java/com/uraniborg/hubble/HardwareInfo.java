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

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HardwareInfo extends BaseInfo {
  public final String TAG = "HWINFO";
  protected String brand;
  protected String oem;
  protected String modelName;
  protected String boardName;
  protected String productName;
  protected String deviceName;
  protected String hardwareName;
  protected String hash;

  public String computeHash() {
    String tag = TAG + "-HASH";
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA256");
    } catch (NoSuchAlgorithmException e) {
      Log.e(tag, "Unable to find SHA256 as hash algorithm.");
      return null;
    }

    // grab all declared field in this class and sort it so that it is reproducible everywhere
    Field[] fields = getSortedFields();
    for (Field field : fields) {
      if (field.getName().equals("TAG") || field.getName().equals("hash")) {
        // we don't take the "TAG" field into consideration.
        continue;
      }
      if (field.getType() == String.class) {
        String fieldValue = null;
        try {
          fieldValue = (String) field.get(this);
          md.update(fieldValue.getBytes());   // this is why sorting the field name is crucial
        } catch (IllegalAccessException e) {
          Log.e(tag, String.format("Failed to get value of field: %s", field.getName()));
          return null;
        }
      }
    }

    return Utilities.convertBytesToHexString(md.digest());
  }

  @Override
  public String toString() {
    String tag = TAG + "-toString";
    StringBuilder sbResult = new StringBuilder();
    Field[] fields = getSortedFields();
    for (Field field : fields) {
      try {
        Object value = field.get(this);
        if (value != null) {
          sbResult.append(String.format("%s: %s\n", field.getName(), value.toString()));
        } else {
          sbResult.append(String.format("%s: %s\n", field.getName(), ""));
        }
      } catch (IllegalAccessException e) {
        Log.e(tag, String.format("Failed to get value of %s", field.getName()));
        sbResult.append(String.format("%s: %s", field.getName(), "ERROR"));
      }
    }
    return sbResult.toString();
  }
}
