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

package core.auth;

import java.util.HashMap;
import java.util.Map;

/** User session model. */
public class UserSession {
  
  private final Long                id;
  private final String              name;
  private final String              authToken;
  private final String              type;
  private final Map<String, Object> claims;
  
  public UserSession(Long id, String name, String authToken, String type) {
    this.id        = id;
    this.name      = name;
    this.authToken = authToken;
    this.type      = type;
    this.claims    = new HashMap<>();
  }
  
  public UserSession(Long id, String name, String authToken, Enum<?> type) {
    this(id, name, authToken, type.name());
  }
  
  public UserSession withClaims(Map<String, Object> claims) {
    this.claims.putAll(claims);
    return this;
  }
  
  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getAuthToken() {
    return authToken;
  }
  
  public String getType() {
    return type;
  }
  
  public Map<String, Object> getClaims() {
    return claims;
  }
  
}
