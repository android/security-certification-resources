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
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class BaseInfo {
  /**
   * Produces a string that is a valid JSON object representing an instance of the class.
   * @param skip An array of strings containing field names to be skipped. NOTE: static and final
   *            fields are automatically skipped.
   * @return a string that is a valid JSON object representing an instance of the class.
   */
  public @Nullable String getJSONString(@Nullable String[] skip) {
    final String TAG = "getJSONString";

    JSONObject jsonObject = getJSON(skip);

    try {
      // because in Java/Android, forward slash is escaped, we want to 'unescape' them
      StringBuilder sbResult = new StringBuilder(jsonObject.toString(2).replace("\\/", "/"));
      return sbResult.toString();

    } catch (JSONException e) {
      Log.e(TAG, String.format("Failed to stringify the JSON object: %s", e.getMessage()));
    }
    return null;
  }

  @NotNull
  private Field[] getRelevantFields(@Nullable String[] skipFieldNames) {
    List<Field> result = new ArrayList<>();

    // convert skipFieldNames to a List first to avoid doing it in a loop
    List<String> skipFieldList = null;
    if (skipFieldNames != null) {
      skipFieldList = Arrays.asList(skipFieldNames);
    }

    for (Field field : this.getClass().getDeclaredFields()) {
      int fieldModifiers = field.getModifiers();
      // we'll skip static fields
      if (Modifier.isStatic(fieldModifiers)) {
        continue;
      }

      // we'll skip final fields, as they are most probably just constants
      if (Modifier.isFinal(fieldModifiers)) {
        continue;
      }

      if (skipFieldList != null) {
        if (skipFieldList.contains(field.getName())) {
          continue;
        }
      }

      result.add(field);
    }

    return result.toArray(new Field[0]);
  }


  private JSONArray convertSetToJSONArray(Set set) {
    JSONArray result = new JSONArray();
    if (set == null) {
      return result;
    }
    for (Object item : set) {
      result.put(item);
    }
    return result;
  }

  private JSONArray convertArrayToJSONArray(Object object) {
    JSONArray result = new JSONArray();
    if (object == null) {
      return result;
    }

    if (object instanceof int[]) {
      int[] array = (int[]) object;
      for (int item : array) {
        result.put(item);
      }
    } else if (object instanceof String[]) {
      String[] array = (String[]) object;
      for (String item : array) {
        result.put(item);
      }
    }

    return result;
  }


  private JSONObject getJSON(@Nullable String[] skip) {
    final String TAG = "getJSON";
    JSONObject result = new JSONObject();

    for (Field field : getRelevantFields(skip)) {
      String fieldName = field.getName();

      try {
        Object value = field.get(this);

        // implement special cases here
        if (field.getType().equals(Set.class)) {
          value = convertSetToJSONArray((Set) value);
        }

        // proofing for null values
        value = (value == null) ? JSONObject.NULL : value;

        if (value.getClass().isArray()) {
          value = convertArrayToJSONArray(value);
        }

        result.put(fieldName, value);
      } catch (IllegalAccessException e) {
        Log.e(TAG, String.format("Failed to access value for field: %s", fieldName));
        continue;
      } catch (JSONException e) {
        Log.e(TAG, String.format("Failed to put value in JSON obj: %s", e.getMessage()));
        continue;
      }
    }

    return result;
  }

  protected Field[] getSortedFields() {
    Field[] result = this.getClass().getDeclaredFields();
    Arrays.sort(result, new SortFieldByName());
    return result;
  }

  /**
   * Custom class that helps sorting of fields in this class.
   */
  class SortFieldByName implements Comparator<Field> {
    @Override
    public int compare(@NotNull Field o1, @NotNull Field o2) {
      return o1.getName().compareTo(o2.getName());
    }
  }
}
