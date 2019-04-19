package core.injections;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static core.controllers.AController.Code;
import static core.controllers.AController.jsonError;

@Singleton
public class CoreErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public CoreErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

//    @Override
//    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
//        exception.printStackTrace();
//        return CompletableFuture.completedFuture(Results.badRequest(jsonError(Code.SERVER_ERROR, exception.getMessage())));
//    }

    @Override
    protected CompletionStage<Result> onNotFound(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(jsonError(Code.NOT_FOUND, message)));
    }

    @Override
    protected CompletionStage<Result> onForbidden(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(jsonError(Code.UNAUTHORIZED, message)));
    }

    @Override
    protected CompletionStage<Result> onBadRequest(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(jsonError(Code.SERVER_ERROR, message)));
    }

}
