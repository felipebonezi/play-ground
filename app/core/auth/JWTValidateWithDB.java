package core.auth;

public interface JWTValidateWithDB {
    boolean isValid(UserSession session);
}
