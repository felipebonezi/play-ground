package core.injections;

import static core.controllers.AController.Code;
import static core.controllers.AController.jsonError;
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

@Singleton
public class CoreErrorHandler extends DefaultHttpErrorHandler {
  
  @Inject
  public CoreErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
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
