/*
 * Copyright 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.exceptions;

/** Basic exception. */
public class CoreException extends Exception {
  
  private final int code;
  
  public CoreException(int code) {
    super();
    this.code = code;
  }
  
  public CoreException(int code, String message) {
    super(message);
    this.code = code;
  }
  
  public int getCode() {
    return code;
  }
  
}
