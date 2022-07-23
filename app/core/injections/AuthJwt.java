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

package core.injections;

import static core.controllers.FormController.Code.UNAUTHORIZED;
import static core.controllers.FormController.jsonError;

import core.auth.UserSession;
import core.auth.session.impl.JwtSessionManagerImpl;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Http authenticator used to check a {@link UserSession} from JWT token
 * over "Authorization" {@link Http.Request} header.
 * If it isn't a valid session then we will return with "401 Unathorized" status code.
 */
@Singleton
public class AuthJwt extends Security.Authenticator {
  
  private final JwtSessionManagerImpl jwtSessionManager;
  
  @Inject
  public AuthJwt(JwtSessionManagerImpl jwtSessionManager) {
    this.jwtSessionManager = jwtSessionManager;
  }
  
  @Override
  public Optional<String> getUsername(Http.Request req) {
    return this.jwtSessionManager.getSession(req).map(UserSession::getName);
  }
  
  @Override
  public Result onUnauthorized(Http.Request req) {
    return unauthorized(jsonError(UNAUTHORIZED));
  }
  
}
