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

public class LibraryInfo extends BaseInfo {
  public static final String TAG = "LIBINFO";

  protected String name;  // the name of the library without full path
  protected String installPath;
  protected String hash;
  protected int bits;   // identifying whether the library is 32 or 64 bits.
  protected long fileSizeInBytes;

  // add here for more known path of system libraries
  protected static final String[] LIB_PATHS = {
      "/system/lib",
      "/system/lib64",
      "/system/product/lib",
      "/system/product/lib64",
      "/system/vendor/lib",
      "/system/vendor/lib64"
  };
}
