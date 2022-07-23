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

package core.forms.binders;

import core.forms.validations.UpdateGroup;
import play.data.validation.Constraints;

/** Update form boirlerplate. */
public abstract class UpdateForm {
  
  @Constraints.Min(value = 1, groups = UpdateGroup.class)
  @Constraints.Required(groups = UpdateGroup.class)
  private Long id;
  
  protected UpdateForm() {
  }
  
  protected UpdateForm(Long id) {
    this.id = id;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
}
