package core.injections;

import core.auth.UserSession;
import core.auth.session.impl.JwtSessionManager;
import core.controllers.AController;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

@Singleton
public class AuthJWT extends Security.Authenticator {
  
  private final JwtSessionManager jwtSessionManager;
  
  @Inject
  public AuthJWT(JwtSessionManager jwtSessionManager) {
    this.jwtSessionManager = jwtSessionManager;
  }
  
  @Override
  public Optional<String> getUsername(Http.Request req) {
    return this.jwtSessionManager.getSession(req).map(UserSession::getName);
  }
  
  @Override
  public Result onUnauthorized(Http.Request req) {
    return Results.unauthorized(AController.jsonError(AController.Code.UNAUTHORIZED));
  }
  
}
