package core.utils;

import com.auth0.jwt.interfaces.Claim;
import core.auth.UserSession;

import java.util.Map;

public final class SessionUtil {
    private SessionUtil() {}

    public static UserSession parse(Map<String, Claim> claims) {
        if (claims.isEmpty()
                || !claims.containsKey("user.id")
                || !claims.containsKey("user.name")
                || !claims.containsKey("user.auth_token")
                || !claims.containsKey("user.type"))
            return null;

        Long userId = claims.get("user.id").asLong();
        String name = claims.get("user.name").asString();
        String authToken = claims.get("user.auth_token").asString();
        String type = claims.get("user.type").asString();
        return new UserSession(userId, name, authToken, type);
    }

}
