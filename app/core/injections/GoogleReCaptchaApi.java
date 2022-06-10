package core.injections;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.typesafe.config.Config;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.shaded.ahc.io.netty.handler.codec.http.HttpResponseStatus;

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
  
  public CompletionStage<Boolean> validate(Http.Request request) {
    if (!this.active) {
      return CompletableFuture.completedFuture(true);
    }
    
    Map<String, String[]> map = request.body().asFormUrlEncoded();
    if (map == null || !map.containsKey("g-recaptcha-response")) {
      return CompletableFuture.completedFuture(false);
    }
    
    String token = map.get("g-recaptcha-response")[0];
    if (Strings.isNullOrEmpty(token)) {
      return CompletableFuture.completedFuture(false);
    }
    
    String remoteIp = request.remoteAddress();
    
    List<String> params = Arrays.asList(
        String.format("secret=%s", this.secret)
        , String.format("response=%s", token)
        , String.format("remoteip=%s", remoteIp)
    );
    String body = String.join("&", params);
    
    return this.wsClient.url(this.url)
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8")
        .post(body)
        .handle((wsResponse, throwable) -> {
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
