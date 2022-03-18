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

import java.lang.reflect.Field;

public class BuildInfo extends BaseInfo{
  public final String TAG = "BUILDINFO";
  protected String bootloaderVersion;
  protected String radioVersion;
  protected String fingerprint;
  protected String kernelVersion;
  protected String securityPatchLevel;
  protected int apiLevel;
  protected String locale;


  @Override
  public String toString() {
    String tag = TAG + "-toString";
    StringBuilder sbResult = new StringBuilder();
    Field[] fields = getSortedFields();
    for (Field field : fields) {
      try {
        sbResult.append(String.format("%s: %s\n", field.getName(), field.get(this).toString()));
      } catch (IllegalAccessException e) {
        Log.e(tag, String.format("Failed to get value of %s", field.getName()));
        sbResult.append(String.format("%s: %s", field.getName(), "ERROR"));
      }
    }
    return sbResult.toString();
  }



}
