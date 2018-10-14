package core.auth;

public class UserSession {

    public Long id;
    public String name;
    public String authToken;
    public String type;

    public UserSession(Long id, String name, String authToken, String type) {
        this.id = id;
        this.name = name;
        this.authToken = authToken;
        this.type = type;
    }

    public UserSession(Long id, String name, String authToken, Enum type) {
        this(id, name, authToken, type.name());
    }

}
