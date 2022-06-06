package core.auth.session;

import core.auth.UserSession;
import java.util.Map;
import java.util.Optional;
import play.mvc.Http;

public interface ISessionManager {
  
  String newSession(Map<String, Object> claims);
  Optional<UserSession> getSession(Http.Request req);
  void removeSession(String authorization);
  
}
