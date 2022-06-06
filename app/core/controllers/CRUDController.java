package core.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.exceptions.CoreException;
import core.forms.DataTableResultForm;
import core.forms.Select2Form;
import core.forms.binders.UpdateForm;
import core.forms.binders.datatable.DataTableForm;
import core.forms.validations.CreateGroup;
import core.forms.validations.UpdateGroup;
import io.ebean.Model;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

public abstract class CRUDController<K extends Model, M extends UpdateForm> extends AController {

    private static final class Parameter {
        static final String ID = "id";
        static final String ENTITY = "entity";
    }

    @Inject
    public HttpExecutionContext context;
    
    protected CRUDController(FormFactory formFactory, SyncCacheApi cacheApi) {
        super(formFactory, cacheApi);
    }

    protected List<Class<?>> getModelCreateGroups() {
        return Arrays.asList(Default.class, CreateGroup.class);
    }

    protected List<Class<?>> getModelUpdateGroups() {
        return Arrays.asList(Default.class, UpdateGroup.class);
    }

    protected abstract Class<M> getModelClass();
    protected abstract DataTableResultForm listEntities(Http.Request req, DataTableForm model) throws CoreException;
    protected abstract Long createEntity(Http.Request req, M model) throws CoreException;
    protected abstract Long updateEntity(Http.Request req, M model) throws CoreException;
    protected abstract M detailEntity(Http.Request req, long entityId) throws CoreException;

    protected void blockEntity(Http.Request req, long entityId) throws CoreException {
        throw new CoreException(Code.SERVER_ERROR, "Not implemented.");
    }
    protected void unblockEntity(Http.Request req, long entityId) throws CoreException {
        throw new CoreException(Code.SERVER_ERROR, "Not implemented.");
    }
    protected void removeEntity(Http.Request req, long entityId) throws CoreException {
        throw new CoreException(Code.SERVER_ERROR, "Not implemented.");
    }
    protected List<Select2Form> searchEntities(Http.Request req, String searchTerm) throws CoreException {
        throw new CoreException(Code.SERVER_ERROR, "Not implemented.");
    }

    public CompletionStage<Result> list(Http.Request req) {
        return CompletableFuture.supplyAsync(() -> {
            Form<DataTableForm> form = this.formFactory.form(DataTableForm.class).bindFromRequest(req);
            if (isNullOrHasError(form))
                return badRequest(jsonError(Code.INVALID_FORM, form));

            DataTableForm model = form.get();

            DataTableResultForm result;
            try {
                result = listEntities(req, model);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonAsDataTable(model, result.recordsTotal, result.recordsFiltered, result.data));
        }, this.context.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create(Http.Request req) {
        return CompletableFuture.supplyAsync(() -> {
            List<Class<?>> groups = getModelCreateGroups();
            Form<M> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest(req);

            if (isNullOrHasError(form))
                return badRequest(jsonError(AController.Code.INVALID_FORM, form));

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

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(Http.Request req) {
        return CompletableFuture.supplyAsync(() -> {
            List<Class<?>> groups = getModelUpdateGroups();
            Form<M> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest(req);
            if (isNullOrHasError(form))
                return badRequest(jsonError(AController.Code.INVALID_FORM, form));

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

    public CompletionStage<Result> detail(Http.Request req, long id) {
        return CompletableFuture.supplyAsync(() -> {
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

    public CompletionStage<Result> block(Http.Request req, long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                blockEntity(req, id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> unblock(Http.Request req, long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                unblockEntity(req, id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> remove(Http.Request req, long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                removeEntity(req, id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> search(Http.Request req, String value) {
        return CompletableFuture.supplyAsync(() -> {
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
