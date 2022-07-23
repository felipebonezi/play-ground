/*
 * Copyright 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.controllers;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import core.forms.binders.datatable.DataTableForm;
import core.injections.AuthJwt;
import core.utils.DebugUtil;
import javax.inject.Inject;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;

/** Abstract controller to be used as default controller inherance. */
public abstract class FormController extends Controller {
  
  private static final   String TAG                 = "AController";
  protected static final String NOT_IMPLEMENTED_YET = "Not implemented yet.";
  
  static final class Parameter {
    
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
  
  /** Project codes used for enhance http response. */
  public static final class Code {
    
    private Code() {
      // Do nothing.
    }
    
    public static final int SUCCESS      = 0;
    public static final int UNAUTHORIZED = 999;
    public static final int SERVER_ERROR = 998;
    public static final int NOT_FOUND    = 997;
    public static final int INVALID_FORM = 996;
    public static final int UNKNOWN      = 995;
    
  }
  
  static final class CacheContext {
    
    private CacheContext() {
    }
    
    protected static final class Expiration {
      
      private Expiration() {
      }
      
      public static final int THIRD_MINUTES = 1800;
      public static final int ONE_HOUR      = 3600;
      public static final int TWO_HOURS     = 7200;
      public static final int EIGHT_HOURS   = 28800;
      public static final int ONE_DAY       = 86400;
      
    }
    
  }
  
  protected final FormFactory  formFactory;
  protected final SyncCacheApi cacheApi;
  
  @Inject
  private AuthJwt authJwt;
  
  protected FormController(FormFactory formFactory, SyncCacheApi cacheApi) {
    this.formFactory = formFactory;
    this.cacheApi    = cacheApi;
  }
  
  /**
   * Verify if http request form has errors.
   *
   * @param form Http request form.
   *
   * @return True if has errors, false otherwise.
   */
  public boolean isNullOrHasError(Form<?> form) {
    boolean invalid = form == null || form.hasErrors() || form.hasGlobalErrors();
    if (invalid && form != null) {
      DebugUtil.debug(TAG, format("Invalid form: %s", form.errorsAsJson()));
    }
    return invalid;
  }
  
  /**
   * Return a wrapped success {@link ObjectNode}.
   *
   * @return JSON with success code.
   */
  public ObjectNode jsonSuccess() {
    ObjectNode json = Json.newObject();
    json.put(Parameter.CODE, Code.SUCCESS);
    return json;
  }
  
  /**
   * Return a wrapped {@link ObjectNode} as DataTable expected response.
   * See more at "Example date" section:
   * <a href="https://datatables.net/manual/server-side">...</a>
   *
   * @param form            Http request form (i.e. used to know if data is filtered or not).
   * @param recordsTotal    Total records, before filtering (i.e. the total number of records in the
   *                        database).
   * @param recordsFiltered Total records, after filtering (i.e. the total number of records after
   *                        filtering has been applied - not just the number of records being
   *                        returned for this page of data).
   * @param data            The data to be displayed in the table. This is an array of data source
   *                        objects, one for each row, which will be used by DataTables.
   *
   * @return DataTable expected JSON response.
   */
  public ObjectNode jsonAsDataTable(DataTableForm form, int recordsTotal, int recordsFiltered,
                                    JsonNode data) {
    ObjectNode json = Json.newObject();
    json.put(Parameter.DRAW, form.getDraw());
    json.put(Parameter.RECORDS_TOTAL, recordsTotal);
    json.put(Parameter.RECORDS_FILTERED, form.isFiltered() ? recordsFiltered : recordsTotal);
    json.set(Parameter.DATA, data);
    return json;
  }
  
  /**
   * Return a wrapped {@link ObjectNode} error without any message to explain it.
   *
   * @param code Error code.
   *
   * @return JSON with error code.
   */
  public static ObjectNode jsonError(int code) {
    return jsonError(code, (String) null);
  }
  
  /**
   * Return a wrapped {@link ObjectNode} with form error as JSON.
   *
   * @param code Error code.
   * @param form Http request form.
   *
   * @return Json with error info.
   */
  public ObjectNode jsonError(int code, Form<?> form) {
    ObjectNode json = Json.newObject();
    json.put(Parameter.CODE, code);
    if (form != null) {
      json.set(Parameter.FORM_ERROR, form.errorsAsJson());
    }
    return json;
  }
  
  /**
   * Return a wrapped {@link ObjectNode} with error message.
   *
   * @param code    Error code.
   * @param message Error message.
   *
   * @return Json with error info.
   */
  public static ObjectNode jsonError(int code, String message) {
    ObjectNode json = Json.newObject();
    json.put(Parameter.CODE, code);
    if (!Strings.isNullOrEmpty(message)) {
      json.put(Parameter.ERROR, message);
    }
    return json;
  }
  
}
