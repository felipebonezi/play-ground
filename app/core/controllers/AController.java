package core.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import core.auth.UserSession;
import core.forms.binders.datatable.DataTableForm;
import core.injections.AuthJWT;
import core.utils.DebugUtil;
import core.utils.SessionUtil;
import core.utils.StringUtil;
import org.joda.time.DateTime;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public abstract class AController extends Controller {

    private static final String TAG = "AController";

    protected static final class Parameter {
        public static final String CODE = "code";
        public static final String DRAW = "draw";
        public static final String RECORDS_TOTAL = "recordsTotal";
        public static final String DATA = "data";
        public static final String RECORDS_FILTERED = "recordsFiltered";
        public static final String FORM_ERROR = "formError";
        public static final String ERROR = "error";
    }

    public static final class Code {
        public static final int SUCCESS = 0;
        public static final int UNAUTHORIZED = 999;
        public static final int SERVER_ERROR = 998;
        public static final int NOT_FOUND = 997;
        public static final int INVALID_FORM = 996;
        public static final int UNKNOWN = 995;
    }

    public static final class CacheContext {
        static final String AUTH_USER = "credentials-ids-%s";

        public static final class Expiration {
            public static final int THIRD_MINUTES = 1800;
            public static final int EIGHT_HOURS = 28800;
        }
    }

    protected final FormFactory formFactory;
    protected final SyncCacheApi cacheApi;

    public AController(FormFactory formFactory, SyncCacheApi cacheApi) {
        this.formFactory = formFactory;
        this.cacheApi = cacheApi;
    }

    public boolean isNullOrHasError(Form<?> form) {
        boolean invalid = form == null || form.hasErrors() || form.hasGlobalErrors();

        if (invalid && form != null) {
            DebugUtil.d(TAG, String.format("Invalid form: %s", form.errorsAsJson()));
        }

        return invalid;
    }

    public ObjectNode jsonSuccess() {
        ObjectNode json = Json.newObject();
        json.put(Parameter.CODE, Code.SUCCESS);
        return json;
    }

    public ObjectNode jsonAsDataTable(DataTableForm form, int recordsTotal, int recordsFiltered, JsonNode array) {
        ObjectNode json = Json.newObject();
        json.put(Parameter.DRAW, form.draw);
        json.put(Parameter.RECORDS_TOTAL, recordsTotal);
        json.put(Parameter.RECORDS_FILTERED, form.isFiltered() ? recordsFiltered : recordsTotal);
        json.set(Parameter.DATA, array);
        return json;
    }

    public static ObjectNode jsonError(int code) {
        return jsonError(code, (String) null);
    }

    public ObjectNode jsonError(int code, Form<?> form) {
        ObjectNode json = Json.newObject();
        json.put(Parameter.CODE, code);
        if (form != null)
            json.set(Parameter.FORM_ERROR, form.errorsAsJson());
        return json;
    }

    public static ObjectNode jsonError(int code, String message) {
        ObjectNode json = Json.newObject();
        json.put(Parameter.CODE, code);
        if (!Strings.isNullOrEmpty(message))
            json.put(Parameter.ERROR, message);
        return json;
    }

    public String createJWT(Map<String, Object> claims) {
        return createJWT(claims, LocalDateTime.now().plusHours(8));
    }

    public String createJWT(Map<String, Object> claims, LocalDateTime expiration) {
        int month = expiration.getMonthValue() - 1;

        Calendar expiresAt = Calendar.getInstance();
        expiresAt.set(expiration.getYear(), month, expiration.getDayOfMonth(), expiration.getHour(), expiration.getMinute());

        String token = null;
        try {
            JWTCreator.Builder builder = JWT.create()
                    .withExpiresAt(expiresAt.getTime())
                    .withIssuer("auth0");
            for (String key : claims.keySet()) {
                Object obj = claims.get(key);
                if (obj instanceof Long)
                    builder.withClaim(key, (Long) obj);
                else if (obj instanceof Integer)
                    builder.withClaim(key, (Integer) obj);
                else if (obj instanceof String)
                    builder.withClaim(key, (String) obj);
                else if (obj instanceof Double)
                    builder.withClaim(key, (Double) obj);
                else if (obj instanceof Boolean)
                    builder.withClaim(key, (Boolean) obj);
                else if (obj instanceof Date)
                    builder.withClaim(key, (Date) obj);
            }
            token = builder.sign(AuthJWT.getJwtAlgorithm());
        } catch (JWTCreationException e){
            DebugUtil.e(e);
        }
        return token;
    }

    public static Map<String, Claim> getClaims(String jwtToken) {
        jwtToken = removeBearer(jwtToken);

        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(AuthJWT.getJwtAlgorithm())
                    .withIssuer("auth0")
                    .acceptNotBefore(DateTime.now().getMillis())
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(jwtToken);
        } catch (JWTVerificationException e){
            //Invalid signature/claims
            DebugUtil.e(e);
        }
        return jwt != null ? jwt.getClaims() : null;
    }

    private static String removeBearer(String jwtToken) {
        if (jwtToken.startsWith("Bearer") || jwtToken.startsWith("Basic")) {
            jwtToken = jwtToken.replace("Bearer", StringUtil.EMPTY).trim();
        }
        return jwtToken;
    }

    public static UserSession getSession(Http.Request req, SyncCacheApi cacheApi) {
        Http.Headers headers = req.getHeaders();
        String jwtToken = headers.get(Http.HeaderNames.AUTHORIZATION).orElse(StringUtil.EMPTY);
        jwtToken = removeBearer(jwtToken);

        String key = String.format(CacheContext.AUTH_USER, jwtToken);
        UserSession session = (UserSession) cacheApi.getOptional(key).orElse(null);

        if (session == null) {
            Map<String, Claim> claims = getClaims(jwtToken);
            session = SessionUtil.parse(claims);

            if (session != null)
                cacheApi.set(key, session, CacheContext.Expiration.THIRD_MINUTES);
        }

        return session;
    }

    public void removeSession(SyncCacheApi cacheApi, String jwtToken) {
        String key = String.format(CacheContext.AUTH_USER, jwtToken);
        cacheApi.remove(key);
    }

}
