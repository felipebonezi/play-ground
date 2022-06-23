package core.injections;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.typesafe.config.Config;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.shaded.ahc.io.netty.handler.codec.http.HttpResponseStatus;

/** Google ReCaptcha implementation. */
@Singleton
public class GoogleReCaptchaApi {
  
  private       boolean  active;
  private       String   url;
  private       String   secret;
  private final WSClient wsClient;
  
  @Inject
  public GoogleReCaptchaApi(Config config, WSClient wsClient) {
    this.wsClient = wsClient;
    if (config.hasPath("api.google.recaptcha.active")) {
      this.active = config.getBoolean("api.google.recaptcha.active");
      this.url    = config.getString("api.google.recaptcha.url");
      this.secret = config.getString("api.google.recaptcha.ws-secret");
    }
  }
  
  /**
   * Validate a {@link Http.Request} through Google ReCaptcha service.
   *
   * @param request Http request with `g-recaptcha-response` form-url-encoded parameter.
   *
   * @return True if is valid, false otherwise.
   */
  public CompletionStage<Boolean> validate(Http.Request request) {
    if (!this.active) {
      return completedFuture(true);
    }
    
    Map<String, String[]> map = request.body().asFormUrlEncoded();
    if (map == null || !map.containsKey("g-recaptcha-response")) {
      return completedFuture(false);
    }
    
    String token = map.get("g-recaptcha-response")[0];
    if (Strings.isNullOrEmpty(token)) {
      return completedFuture(false);
    }
    
    List<String> params =
        asList("secret=" + this.secret, "response=" + token, "remoteip=" + request.remoteAddress());
    
    return this.wsClient.url(this.url)
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8")
        .post(String.join("&", params)).handle((wsResponse, throwable) -> {
          if (throwable != null) {
            throwable.printStackTrace();
            return false;
          }
          
          int status = wsResponse.getStatus();
          if (status != HttpResponseStatus.OK.code()) {
            return false;
          }
          
          JsonNode response = wsResponse.asJson();
          return response.get("success").asBoolean(false);
        });
  }
  
}
