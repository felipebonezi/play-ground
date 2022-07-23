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

package core.auth.session;

import core.auth.UserSession;
import java.util.Map;
import java.util.Optional;
import play.mvc.Http;

/** Session manager interface to create, get and remove user session from {@link Http.Request}. */
public interface SessionManager {
  
  String newSession(Map<String, Object> claims);
  
  Optional<UserSession> getSession(Http.Request req);
  
  void removeSession(String authorization);
  
}
