package core.auth;

public class UserSession {

    public Long id;
    public String name;
    public String authToken;

    public UserSession(Long id, String name, String authToken) {
        this.id = id;
        this.name = name;
        this.authToken = authToken;
    }

}
