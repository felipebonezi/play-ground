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

import static core.controllers.FormController.Code;
import static core.controllers.FormController.jsonError;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Results.badRequest;

import com.typesafe.config.Config;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

/** Default Http error handler maping response as JSON. */
@Singleton
public class CoreErrorHandler extends DefaultHttpErrorHandler {
  
  @Inject
  public CoreErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper,
                          Provider<Router> routes) {
    super(config, environment, sourceMapper, routes);
  }
  
  @Override
  public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
    exception.printStackTrace();
    if (request.accepts(Http.MimeTypes.JSON)) {
      return completedFuture(badRequest(jsonError(Code.SERVER_ERROR, exception.getMessage())));
    }
    return super.onServerError(request, exception);
  }
  
  @Override
  protected CompletionStage<Result> onNotFound(Http.RequestHeader request, String message) {
    return completedFuture(badRequest(jsonError(Code.NOT_FOUND, message)));
  }
  
  @Override
  protected CompletionStage<Result> onForbidden(Http.RequestHeader request, String message) {
    return completedFuture(badRequest(jsonError(Code.UNAUTHORIZED, message)));
  }
  
  @Override
  protected CompletionStage<Result> onBadRequest(Http.RequestHeader request, String message) {
    return completedFuture(badRequest(jsonError(Code.SERVER_ERROR, message)));
  }
  
}
