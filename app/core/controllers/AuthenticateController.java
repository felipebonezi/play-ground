package core.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import core.forms.LoginForm;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class AuthenticateController<T> extends AController {

    public AuthenticateController(FormFactory formFactory, SyncCacheApi cacheApi) {
        super(formFactory, cacheApi);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> login() {
        Form<LoginForm> form = this.formFactory.form(LoginForm.class).bindFromRequest();
        if (isNullOrHasError(form)) {
            return CompletableFuture.completedFuture(badRequest(jsonError(AController.Code.INVALID_FORM, form)));
        }

        LoginForm aModel = form.get();

        return CompletableFuture.supplyAsync(() -> {
            T model = check(aModel);

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
        });
    }

    protected abstract T check(LoginForm form);

    protected void saveToken(T model, String token) {}

    protected Map<String, Object> getClaims(T model) {
        return new HashMap<>();
    }

}
