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

package core.utils;

import com.google.common.base.Strings;
import java.text.Normalizer;

/** String helper class. */
public final class StringUtil {
  
  private StringUtil() {
  }
  
  public static final String EMPTY = "";
  public static final String COMMA = ",";
  public static final String SPACE = " ";
  public static final String DASH  = "-";
  
  /**
   * Transform string to numbers.
   *
   * @param text Text to transform.
   *
   * @return Only numbers.
   */
  public static String toOnlyNumbers(String text) {
    if (Strings.isNullOrEmpty(text)) {
      return text;
    }
    
    return text.replaceAll("\\D", "");
  }
  
  /**
   * Return string to ASCII char table.
   *
   * @param text Text.
   *
   * @return Text as ASCII.
   */
  public static String toNormalized(String text) {
    if (Strings.isNullOrEmpty(text)) {
      return "";
    }
    
    return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }
  
}
