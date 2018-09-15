package core.injections;

import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
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
    public String getUsername(Http.Context context) {
        String token = context.request().header(Http.HeaderNames.AUTHORIZATION).orElse(null);
        if (Strings.isNullOrEmpty(token))
            return null;

        UserSession session = AController.getSession(this.cacheApi);
        if (session != null && this.validateWithDB != null && this.validateWithDB.isValid(session)) {
            return session.name;
        }

        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return AController.unauthorized(AController.jsonError(AController.Code.UNAUTHORIZED));
    }

    public static Algorithm getJwtAlgorithm() {
        return JWT_ALGORITHM;
    }

    public interface JWTValidateWithDB {
        boolean isValid(UserSession session);
    }

    public static class DefaultJWTValidateWithDB implements JWTValidateWithDB {

        @Override
        public boolean isValid(UserSession session) {
            return session != null && !Strings.isNullOrEmpty(session.authToken);
        }

    }

}
