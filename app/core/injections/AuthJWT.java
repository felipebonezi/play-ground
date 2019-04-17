package core.injections;

import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import core.auth.JWTValidateWithDB;
import core.auth.UserSession;
import core.controllers.AController;
import core.utils.DebugUtil;
import play.cache.SyncCacheApi;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Singleton
public class AuthJWT extends Security.Authenticator {

    private static Algorithm JWT_ALGORITHM;
    private final SyncCacheApi cacheApi;
    private final JWTValidateWithDB validateWithDB;

    @Inject
    public AuthJWT(Config config, SyncCacheApi cacheApi, JWTValidateWithDB validateWithDB) {
        this.cacheApi = cacheApi;
        this.validateWithDB = validateWithDB;

        String jwtSecret = config.getString("play.jwt.secret.key");
        try {
            JWT_ALGORITHM = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            DebugUtil.e(e);
        }
    }

    @Override
    public Optional<String> getUsername(Http.Request req) {
        String token = req.header(Http.HeaderNames.AUTHORIZATION).orElse(null);
        if (Strings.isNullOrEmpty(token))
            return Optional.empty();

        UserSession session = AController.getSession(req, this.cacheApi);
        if (session == null)
            return Optional.empty();

        return Optional.of(session.name);
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        return AController.unauthorized(AController.jsonError(AController.Code.UNAUTHORIZED));
    }

    public static Algorithm getJwtAlgorithm() {
        return JWT_ALGORITHM;
    }

}
