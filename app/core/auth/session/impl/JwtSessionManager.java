package core.auth.session.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import core.auth.JWTValidateWithDB;
import core.auth.UserSession;
import core.auth.session.ISessionManager;
import core.controllers.AController;
import core.utils.DebugUtil;
import core.utils.SessionUtil;
import core.utils.StringUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.joda.time.DateTime;
import play.cache.SyncCacheApi;
import play.mvc.Http;

public class JwtSessionManager implements ISessionManager {
  
  private static final String CACHE_AUTH_USER = "credentials-ids-%s";
  
  private final Config            config;
  private final Algorithm         jwtAlgorithm;
  private final SyncCacheApi      cacheApi;
  private final JWTValidateWithDB validateWithDB;
  
  @Inject
  public JwtSessionManager(Config config, SyncCacheApi cacheApi, JWTValidateWithDB validateWithDB) {
    this.config         = config;
    this.cacheApi       = cacheApi;
    this.validateWithDB = validateWithDB;
    
    String jwtSecret = config.getString("play.jwt.secret.key");
    if (Strings.isNullOrEmpty(jwtSecret)) {
      throw new IllegalArgumentException("Valid secret required. " +
          "Define into application.conf over 'play.jwt.secret.key' config.");
    }
    
    this.jwtAlgorithm = Algorithm.HMAC256(jwtSecret);
  }
  
  private Algorithm getJwtAlgorithm() {
    return this.jwtAlgorithm;
  }
  
  private static String removeBearer(String jwtToken) {
    if (jwtToken.startsWith("Bearer") || jwtToken.startsWith("Basic")) {
      jwtToken = jwtToken.replace("Bearer", StringUtil.EMPTY).trim();
    }
    return jwtToken;
  }
  
  private Map<String, Claim> getClaims(String jwtToken) {
    jwtToken = removeBearer(jwtToken);
    
    DecodedJWT jwt = null;
    try {
      JWTVerifier verifier = JWT.require(getJwtAlgorithm())
          .withIssuer("auth0")
          .acceptNotBefore(DateTime.now().getMillis())
          .build(); //Reusable verifier instance
      jwt = verifier.verify(jwtToken);
    } catch (JWTVerificationException e) {
      //Invalid signature/claims
      DebugUtil.e(e);
    }
    return jwt != null ? jwt.getClaims() : Collections.emptyMap();
  }
  
  @SuppressWarnings("MagicConstant")
  @Override
  public String newSession(Map<String, Object> claims) {
    Duration      duration   = this.config.getDuration("play.jwt.expiresIn");
    LocalDateTime expiration = LocalDateTime.now().minus(duration);
    int           month      = expiration.getMonthValue() - 1;
    
    Calendar expiresAt = Calendar.getInstance();
    expiresAt.set(expiration.getYear(), month, expiration.getDayOfMonth(), expiration.getHour(), expiration.getMinute());
    
    String token = null;
    try {
      JWTCreator.Builder builder = JWT.create()
          .withExpiresAt(expiresAt.getTime())
          .withIssuer("auth0");
      for (Map.Entry<String, Object> entry : claims.entrySet()) {
        String key = entry.getKey();
        Object obj = entry.getValue();
        if (obj instanceof Long) {
          builder.withClaim(key, (Long) obj);
        } else if (obj instanceof Integer) {
          builder.withClaim(key, (Integer) obj);
        } else if (obj instanceof String) {
          builder.withClaim(key, (String) obj);
        } else if (obj instanceof Double) {
          builder.withClaim(key, (Double) obj);
        } else if (obj instanceof Boolean) {
          builder.withClaim(key, (Boolean) obj);
        } else if (obj instanceof Date) {
          builder.withClaim(key, (Date) obj);
        }
      }
      token = builder.sign(getJwtAlgorithm());
    } catch (JWTCreationException e) {
      DebugUtil.e(e);
    }
    return token;
  }
  
  @Override
  public Optional<UserSession> getSession(Http.Request req) {
    String jwtToken =
        removeBearer(req.getHeaders().get(Http.HeaderNames.AUTHORIZATION).orElse(StringUtil.EMPTY));
    
    if (Strings.isNullOrEmpty(jwtToken)) {
      // Valid JWT token is required.
      return Optional.empty();
    }
    
    String      key           = String.format(CACHE_AUTH_USER, jwtToken);
    UserSession sessionCached = (UserSession) cacheApi.get(key).orElse(null);
    
    if (sessionCached != null && !validateWithDB.isValid(sessionCached)) {
      // Valid JWT token is required.
      return Optional.empty();
    }
    
    Map<String, Claim> claims     = getClaims(jwtToken);
    UserSession        newSession = SessionUtil.parse(claims);
    
    if (newSession != null) {
      cacheApi.set(key, newSession, AController.CacheContext.Expiration.THIRD_MINUTES);
    }
    
    return Optional.ofNullable(newSession);
  }
  
  @Override
  public void removeSession(String authorization) {
    String key = String.format(CACHE_AUTH_USER, authorization);
    cacheApi.remove(key);
  }
  
}
