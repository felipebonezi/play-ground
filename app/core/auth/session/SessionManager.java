package core.auth.session;

import core.auth.UserSession;
import java.util.Map;
import java.util.Optional;
import play.mvc.Http;

/** Session manager interface to create, get and remove user session from {@link Http.Request}. */
public interface SessionManager {
  
  String newSession(Map<String, Object> claims);
  
  Optional<UserSession> getSession(Http.Request req);
  
  void removeSession(String authorization);
  
}
