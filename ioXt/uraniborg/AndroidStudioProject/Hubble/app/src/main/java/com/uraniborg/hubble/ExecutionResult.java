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

/**
 * ExecutionResult. This class is created to encapsulate various return values and the state of
 * execution from issuing/exec-ing a shell command. Refer to {@link Utilities#executeInShell(String)}
 */
public class ExecutionResult {
  public Integer exitCode = null;
  public String stdOutStr = null;
  public String stdErrStr = null;
  public String exceptionMessage = null;
  public boolean exceptionTriggered = false;
}
