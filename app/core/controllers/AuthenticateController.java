package core.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import core.forms.LoginForm;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class AuthenticateController<T> extends AController {

    @Inject
    public HttpExecutionContext context;

    public AuthenticateController(FormFactory formFactory, SyncCacheApi cacheApi) {
        super(formFactory, cacheApi);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> login(Http.Request req) {
        return CompletableFuture.supplyAsync(() -> {
            Form<LoginForm> form = this.formFactory.form(LoginForm.class).bindFromRequest(req);
            if (isNullOrHasError(form)) {
                return badRequest(jsonError(AController.Code.INVALID_FORM, form));
            }

            T model = check(req, form.get());

            if (model == null)
                return badRequest(jsonError(Code.UNAUTHORIZED));

            String authToken = UUID.randomUUID().toString();
            saveToken(model, authToken);

            Map<String, Object> claims = getClaims(model);
            claims.put("user.auth_token", authToken);
            String jwt = createJWT(claims);

            Http.Cookie cookie = Http.Cookie.builder(Http.HeaderNames.AUTHORIZATION, jwt)
                    .withHttpOnly(true)
                    .build();

            ObjectNode json = jsonSuccess();
            json.put(Http.HeaderNames.AUTHORIZATION, jwt);

            return ok(json).withCookies(cookie);
        }, this.context.current());
    }

    protected abstract T check(Http.Request req, LoginForm form);

    protected void saveToken(T model, String token) {}

    protected Map<String, Object> getClaims(T model) {
        return new HashMap<>();
    }

}
