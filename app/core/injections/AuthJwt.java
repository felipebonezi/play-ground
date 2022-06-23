package core.injections;

import static core.controllers.FormController.Code.UNAUTHORIZED;
import static core.controllers.FormController.jsonError;

import core.auth.UserSession;
import core.auth.session.impl.JwtSessionManagerImpl;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Http authenticator used to check a {@link UserSession} from JWT token
 * over "Authorization" {@link Http.Request} header.
 * If it isn't a valid session then we will return with "401 Unathorized" status code.
 */
@Singleton
public class AuthJwt extends Security.Authenticator {
  
  private final JwtSessionManagerImpl jwtSessionManager;
  
  @Inject
  public AuthJwt(JwtSessionManagerImpl jwtSessionManager) {
    this.jwtSessionManager = jwtSessionManager;
  }
  
  @Override
  public Optional<String> getUsername(Http.Request req) {
    return this.jwtSessionManager.getSession(req).map(UserSession::getName);
  }
  
  @Override
  public Result onUnauthorized(Http.Request req) {
    return unauthorized(jsonError(UNAUTHORIZED));
  }
  
}
