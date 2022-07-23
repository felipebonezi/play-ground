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

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.exceptions.CoreException;
import core.forms.DataTableResultForm;
import core.forms.Select2Form;
import core.forms.binders.UpdateForm;
import core.forms.binders.datatable.DataTableForm;
import core.forms.validations.CreateGroup;
import core.forms.validations.UpdateGroup;
import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.validation.groups.Default;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Basic CRUD controller implementation.
 *
 * @param <M> Http request body.
 */
public abstract class CrudController<M extends UpdateForm> extends FormController {
  
  private static final class Parameter {
    
    private Parameter() {
    }
    
    static final String ID     = "id";
    static final String ENTITY = "entity";
    
  }
  
  @Inject
  public HttpExecutionContext context;
  
  protected CrudController(FormFactory formFactory, SyncCacheApi cacheApi) {
    super(formFactory, cacheApi);
  }
  
  protected List<Class<?>> getModelCreateGroups() {
    return asList(Default.class, CreateGroup.class);
  }
  
  protected List<Class<?>> getModelUpdateGroups() {
    return asList(Default.class, UpdateGroup.class);
  }
  
  protected abstract Class<M> getModelClass();
  
  protected abstract DataTableResultForm listEntities(Http.Request req, DataTableForm model)
      throws CoreException;
  
  protected abstract Long createEntity(Http.Request req, M model) throws CoreException;
  
  protected abstract Long updateEntity(Http.Request req, M model) throws CoreException;
  
  protected abstract M detailEntity(Http.Request req, long entityId) throws CoreException;
  
  protected void blockEntity(Http.Request req, long entityId) throws CoreException {
    throw new CoreException(Code.SERVER_ERROR, NOT_IMPLEMENTED_YET);
  }
  
  protected void unblockEntity(Http.Request req, long entityId) throws CoreException {
    throw new CoreException(Code.SERVER_ERROR, NOT_IMPLEMENTED_YET);
  }
  
  protected void removeEntity(Http.Request req, long entityId) throws CoreException {
    throw new CoreException(Code.SERVER_ERROR, NOT_IMPLEMENTED_YET);
  }
  
  protected List<Select2Form> searchEntities(Http.Request req, String searchTerm)
      throws CoreException {
    throw new CoreException(Code.SERVER_ERROR, NOT_IMPLEMENTED_YET);
  }
  
  /**
   * List route.
   *
   * @param req Http request.
   *
   * @return JSON using DataTable response format.
   */
  public CompletionStage<Result> list(Http.Request req) {
    return supplyAsync(() -> {
      Form<DataTableForm> form = this.formFactory.form(DataTableForm.class).bindFromRequest(req);
      if (isNullOrHasError(form)) {
        return badRequest(jsonError(Code.INVALID_FORM, form));
      }
      
      DataTableForm model = form.get();
      
      DataTableResultForm result;
      try {
        result = listEntities(req, model);
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      return ok(jsonAsDataTable(model, result.getRecordsTotal(), result.getRecordsFiltered(),
          result.getData()));
    }, this.context.current());
  }
  
  /**
   * Create route.
   *
   * @param req Http request.
   *
   * @return Status "200 OK" with entity Id.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public CompletionStage<Result> create(Http.Request req) {
    return supplyAsync(() -> {
      List<Class<?>> groups = getModelCreateGroups();
      Form<M> form =
          this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()]))
              .bindFromRequest(req);
      
      if (isNullOrHasError(form)) {
        return badRequest(jsonError(FormController.Code.INVALID_FORM, form));
      }
      
      Long entityId;
      try {
        entityId = createEntity(req, form.get());
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      
      ObjectNode json = jsonSuccess();
      json.put(Parameter.ID, entityId);
      return ok(json);
    }, this.context.current());
  }
  
  /**
   * Update route.
   *
   * @param req Http request.
   *
   * @return Status "200 OK" with entity Id.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public CompletionStage<Result> update(Http.Request req) {
    return supplyAsync(() -> {
      List<Class<?>> groups = getModelUpdateGroups();
      Form<M> form =
          this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()]))
              .bindFromRequest(req);
      if (isNullOrHasError(form)) {
        return badRequest(jsonError(FormController.Code.INVALID_FORM, form));
      }
      
      Long entityId;
      try {
        entityId = updateEntity(req, form.get());
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      
      ObjectNode json = jsonSuccess();
      json.put(Parameter.ID, entityId);
      return ok(json);
    }, this.context.current());
  }
  
  /**
   * Details route.
   *
   * @param req Http request.
   * @param id  Entity id.
   *
   * @return Status "200 OK" with entity as JSON.
   */
  public CompletionStage<Result> detail(Http.Request req, long id) {
    return supplyAsync(() -> {
      M model;
      try {
        model = detailEntity(req, id);
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      
      ObjectNode json = jsonSuccess();
      json.set(Parameter.ENTITY, Json.toJson(model));
      return ok(json);
    }, this.context.current());
  }
  
  /**
   * Block route.
   *
   * @param req Http request.
   * @param id  Entity id.
   *
   * @return Status "200 OK".
   */
  public CompletionStage<Result> block(Http.Request req, long id) {
    return supplyAsync(() -> {
      try {
        blockEntity(req, id);
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      return ok(jsonSuccess());
    }, this.context.current());
  }
  
  /**
   * Unblock route.
   *
   * @param req Http request.
   * @param id  Entity id.
   *
   * @return Status "200 OK".
   */
  public CompletionStage<Result> unblock(Http.Request req, long id) {
    return supplyAsync(() -> {
      try {
        unblockEntity(req, id);
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      return ok(jsonSuccess());
    }, this.context.current());
  }
  
  /**
   * Remove route.
   *
   * @param req Http request.
   * @param id  Entity id.
   *
   * @return Status "200 OK".
   */
  public CompletionStage<Result> remove(Http.Request req, long id) {
    return supplyAsync(() -> {
      try {
        removeEntity(req, id);
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      return ok(jsonSuccess());
    }, this.context.current());
  }
  
  /**
   * Search entity by value.
   *
   * @param req   Http request.
   * @param value Search value.
   *
   * @return Status "200 OK" as JSON array.
   */
  public CompletionStage<Result> search(Http.Request req, String value) {
    return supplyAsync(() -> {
      JsonNode json;
      try {
        json = Json.toJson(searchEntities(req, value));
      } catch (CoreException e) {
        return badRequest(jsonError(e.getCode(), e.getMessage()));
      }
      return ok(json);
    }, this.context.current());
  }
  
}
