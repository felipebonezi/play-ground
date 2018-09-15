package core.utils;

import com.auth0.jwt.interfaces.Claim;
import core.auth.UserSession;

import java.util.Map;

public final class SessionUtil {

    public static UserSession parse(Map<String, Claim> claims) {
        if (claims == null
                || claims.isEmpty()
                || !claims.containsKey("user.id")
                || !claims.containsKey("user.name")
                || !claims.containsKey("user.auth_token"))
            return null;

        Long userId = claims.get("user.id").asLong();
        String name = claims.get("user.name").asString();
        String authToken = claims.get("user.auth_token").asString();
        return new UserSession(userId, name, authToken);
    }

}
