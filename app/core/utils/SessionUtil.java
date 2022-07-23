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

import com.auth0.jwt.interfaces.Claim;
import core.auth.UserSession;
import java.util.Map;

/** Session helper methods. */
public final class SessionUtil {
  
  private SessionUtil() {
  }
  
  /**
   * Get {@link UserSession} from JWT claims map.
   *
   * @param claims Claims.
   *
   * @return User session.
   */
  public static UserSession parse(Map<String, Claim> claims) {
    if (claims.isEmpty()
        || !claims.containsKey("user.id")
        || !claims.containsKey("user.name")
        || !claims.containsKey("user.auth_token")
        || !claims.containsKey("user.type")) {
      return null;
    }
    
    Long   userId    = claims.get("user.id").asLong();
    String name      = claims.get("user.name").asString();
    String authToken = claims.get("user.auth_token").asString();
    String type      = claims.get("user.type").asString();
    return new UserSession(userId, name, authToken, type);
  }
  
}
