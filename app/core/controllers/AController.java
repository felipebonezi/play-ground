package core.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import core.forms.binders.datatable.DataTableForm;
import core.injections.AuthJWT;
import core.utils.DebugUtil;
import javax.inject.Inject;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;

public abstract class AController extends Controller {

    private static final String TAG = "AController";
    
    protected static final class Parameter {
        
        private Parameter() {
        }
        
        public static final String CODE             = "code";
        public static final String DRAW             = "draw";
        public static final String RECORDS_TOTAL    = "recordsTotal";
        public static final String DATA             = "data";
        public static final String RECORDS_FILTERED = "recordsFiltered";
        public static final String FORM_ERROR       = "formError";
        public static final String ERROR            = "error";
        
    }
    
    public static final class Code {
        
        private Code() {
        }
        
        public static final int SUCCESS      = 0;
        public static final int UNAUTHORIZED = 999;
        public static final int SERVER_ERROR = 998;
        public static final int NOT_FOUND    = 997;
        public static final int INVALID_FORM = 996;
        public static final int UNKNOWN      = 995;
        
    }
    
    public static final class CacheContext {
        
        private CacheContext() {
        }
        
        public static final class Expiration {
            
            private Expiration() {
            }
            
            public static final int THIRD_MINUTES = 1800;
            public static final int EIGHT_HOURS   = 28800;
            
        }
        
    }
    
    protected final FormFactory  formFactory;
    protected final SyncCacheApi cacheApi;
    
    @Inject
    private AuthJWT authJWT;
    
    protected AController(FormFactory formFactory, SyncCacheApi cacheApi) {
        this.formFactory = formFactory;
        this.cacheApi    = cacheApi;
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

}
