package core.auth;

/** JWT validation using a database. */
public interface JwtValidateWithDb {
  
  boolean isValid(UserSession session);
  
}
