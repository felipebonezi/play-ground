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

package core.forms.binders.datatable;

/** DataTable order form. */
public class DataTableOrderForm {
  
  private int    column;
  private String dir;
  
  public int getColumn() {
    return column;
  }
  
  public void setColumn(int column) {
    this.column = column;
  }
  
  public String getDir() {
    return dir;
  }
  
  public void setDir(String dir) {
    this.dir = dir;
  }
  
}
