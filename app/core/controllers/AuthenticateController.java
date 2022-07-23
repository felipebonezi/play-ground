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

package core.controllers;

import static core.controllers.FormController.Code.INVALID_FORM;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.fasterxml.jackson.databind.node.ObjectNode;
import core.auth.session.impl.JwtSessionManagerImpl;
import core.forms.LoginForm;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Basic authentication controller implementation.
 *
 * @param <T> Entity model (e.g. User).
 */
public abstract class AuthenticateController<T> extends FormController {
  
  @Inject
  public  HttpExecutionContext  context;
  @Inject
  private JwtSessionManagerImpl jwtSessionManager;
  
  protected AuthenticateController(FormFactory formFactory, SyncCacheApi cacheApi) {
    super(formFactory, cacheApi);
  }
  
  /**
   * Login route.
   *
   * @param req Http request.
   *
   * @return If it has a valid {@link T} entity then a session will be provided.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public CompletionStage<Result> login(Http.Request req) {
    return supplyAsync(() -> {
      Form<LoginForm> form = this.formFactory.form(LoginForm.class).bindFromRequest(req);
      if (isNullOrHasError(form)) {
        return badRequest(jsonError(INVALID_FORM, form));
      }
      
      T model = check(req, form.get());
      
      if (model == null) {
        return unauthorized(jsonError(Code.UNAUTHORIZED));
      }
      
      String authToken = UUID.randomUUID().toString();
      saveToken(model, authToken);
      
      Map<String, Object> claims = getClaims(model);
      claims.put("user.auth_token", authToken);
      String jwt = this.jwtSessionManager.newSession(claims);
      
      Http.Cookie cookie = Http.Cookie.builder(AUTHORIZATION, jwt).withHttpOnly(true).build();
      
      ObjectNode json = jsonSuccess();
      json.put(AUTHORIZATION, jwt);
      
      return ok(json).withCookies(cookie);
    }, this.context.current());
  }
  
  protected abstract T check(Http.Request req, LoginForm form);
  
  protected void saveToken(T model, String token) {
    // Do nothing.
  }
  
  protected Map<String, Object> getClaims(T model) {
    return Collections.emptyMap();
  }
  
}
