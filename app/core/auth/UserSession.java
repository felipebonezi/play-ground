package core.auth;

import java.util.HashMap;
import java.util.Map;

public class UserSession {

    public Long id;
    public String name;
    public String authToken;
    public String type;
    private final Map<String, Object> claims;

    public UserSession(Long id, String name, String authToken, String type) {
        this.id = id;
        this.name = name;
        this.authToken = authToken;
        this.type = type;
        this.claims = new HashMap<>();
    }

    public UserSession(Long id, String name, String authToken, Enum<?> type) {
        this(id, name, authToken, type.name());
    }
    
    public UserSession withClaims(Map<String, Object> claims) {
        this.claims.putAll(claims);
        return this;
    }
    
    public Map<String, Object> getClaims() {
        return claims;
    }
    
}
