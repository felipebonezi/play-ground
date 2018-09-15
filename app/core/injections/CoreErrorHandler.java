package core.injections;

import com.typesafe.config.Config;
import core.controllers.AController;
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

@Singleton
public class CoreErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public CoreErrorHandler(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        exception.printStackTrace();
        return CompletableFuture.completedFuture(Results.badRequest(AController.jsonError(AController.Code.SERVER_ERROR, exception.getMessage())));
    }

    @Override
    protected CompletionStage<Result> onNotFound(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(AController.jsonError(AController.Code.NOT_FOUND, message)));
    }

    @Override
    protected CompletionStage<Result> onForbidden(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(AController.jsonError(AController.Code.UNAUTHORIZED, message)));
    }

    @Override
    protected CompletionStage<Result> onBadRequest(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.badRequest(AController.jsonError(AController.Code.SERVER_ERROR, message)));
    }
}
