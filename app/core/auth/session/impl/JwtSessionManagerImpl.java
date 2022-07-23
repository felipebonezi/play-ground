package core.auth.session.impl;

import static core.utils.StringUtil.EMPTY;
import static java.util.Collections.emptyMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import core.auth.JwtValidateWithDb;
import core.auth.UserSession;
import core.auth.session.SessionManager;
import core.utils.DebugUtil;
import core.utils.SessionUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import play.cache.SyncCacheApi;
import play.mvc.Http;

/** JWT session manager implementation. */
public class JwtSessionManagerImpl implements SessionManager {
  
  private static final String BEARER             = "Bearer";
  private static final String BASIC              = "Basic";
  private static final String CACHE_AUTH_USER    = "credentials-ids-%s";
  private static final int    EXPIRATION_IN_SECS = 1800;
  
  private final Config            config;
  private final Algorithm         jwtAlgorithm;
  private final SyncCacheApi      cacheApi;
  private final JwtValidateWithDb validateWithDb;
  
  @Inject
  public JwtSessionManagerImpl(Config config, SyncCacheApi cacheApi,
                               JwtValidateWithDb validateWithDb) {
    this.config         = config;
    this.cacheApi       = cacheApi;
    this.validateWithDb = validateWithDb;
    
    String jwtSecret = config.getString("play.jwt.secret.key");
    if (Strings.isNullOrEmpty(jwtSecret)) {
      throw new IllegalArgumentException("Valid secret required. "
          + "Define into application.conf over 'play.jwt.secret.key' config.");
    }
    
    this.jwtAlgorithm = Algorithm.HMAC256(jwtSecret);
  }
  
  private Algorithm getJwtAlgorithm() {
    return this.jwtAlgorithm;
  }
  
  private static String removeBearer(String jwtToken) {
    return jwtToken.replace(BEARER, EMPTY).replace(BASIC, EMPTY).trim();
  }
  
  private Map<String, Claim> getClaims(String jwtToken) {
    String sanitizedJwtToken = removeBearer(jwtToken);
    
    try {
      JWTVerifier verifier = JWT.require(getJwtAlgorithm())
          .withIssuer("auth0")
          .acceptNotBefore(EXPIRATION_IN_SECS)
          .build(); //Reusable verifier instance
      return verifier.verify(sanitizedJwtToken).getClaims();
    } catch (JWTVerificationException e) {
      // Invalid signature/claims.
      DebugUtil.error(e);
      return emptyMap();
    }
  }
  
  @Override
  public String newSession(Map<String, Object> claims) {
    Duration      duration   = this.config.getDuration("play.jwt.expiresIn");
    LocalDateTime expiration = LocalDateTime.now().minus(duration);
    
    String token = null;
    try {
      token = JWT.create()
          .withExpiresAt(expiration.toInstant(ZoneOffset.UTC))
          .withIssuer("auth0")
          .withPayload(claims)
          .sign(getJwtAlgorithm());
    } catch (JWTCreationException e) {
      DebugUtil.error(e);
    }
    return token;
  }
  
  @Override
  public Optional<UserSession> getSession(Http.Request req) {
    String jwtToken =
        removeBearer(req.getHeaders().get(Http.HeaderNames.AUTHORIZATION).orElse(EMPTY));
    
    if (Strings.isNullOrEmpty(jwtToken)) {
      // Valid JWT token is required.
      return Optional.empty();
    }
    
    String      key           = String.format(CACHE_AUTH_USER, jwtToken);
    UserSession sessionCached = (UserSession) this.cacheApi.get(key).orElse(null);
    
    if (sessionCached != null && !validateWithDb.isValid(sessionCached)) {
      // Valid JWT token is required.
      return Optional.empty();
    }
    
    Map<String, Claim> claims     = getClaims(jwtToken);
    UserSession        newSession = SessionUtil.parse(claims);
    
    if (newSession != null) {
      // 30 minutes cached session.
      this.cacheApi.set(key, newSession, EXPIRATION_IN_SECS);
    }
    
    return Optional.ofNullable(newSession);
  }
  
  @Override
  public void removeSession(String authorization) {
    String key = String.format(CACHE_AUTH_USER, authorization);
    this.cacheApi.remove(key);
  }
  
}
