package core.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.auth.UserSession;
import core.exceptions.CoreException;
import core.forms.DataTableResultForm;
import core.forms.Select2Form;
import core.forms.binders.UpdateForm;
import core.forms.binders.datatable.DataTableForm;
import core.forms.validations.CreateGroup;
import core.forms.validations.UpdateGroup;
import io.ebean.Model;
import play.cache.SyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.inject.Inject;
import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class CRUDController<K extends Model, M extends UpdateForm> extends AController {

    private static final String TAG = "CRUDController";

    private static final class Parameter {
        static final String ID = "id";
        static final String ENTITY = "entity";
    }

    @Inject
    public HttpExecutionContext context;

    public CRUDController(FormFactory formFactory, SyncCacheApi cacheApi) {
        super(formFactory, cacheApi);
    }

    public UserSession getSession() {
        return getSession(this.cacheApi);
    }

    protected List<Class<?>> getModelCreateGroups() {
        return Arrays.asList(Default.class, CreateGroup.class);
    }

    protected List<Class<?>> getModelUpdateGroups() {
        return Arrays.asList(Default.class, UpdateGroup.class);
    }

    protected abstract Class<M> getModelClass();
    protected abstract DataTableResultForm listEntities(DataTableForm model) throws CoreException;
    protected abstract Long createEntity(M model) throws CoreException;
    protected abstract Long updateEntity(M model) throws CoreException;
    protected abstract M detailEntity(long entityId) throws CoreException;

    protected void blockEntity(long entityId) throws CoreException {}
    protected void unblockEntity(long entityId) throws CoreException {}
    protected void removeEntity(long entityId) throws CoreException {}
    protected List<Select2Form> searchEntities(String searchTerm) throws CoreException {
        return Collections.emptyList();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> list() {
        Form<DataTableForm> form = this.formFactory.form(DataTableForm.class).bindFromRequest();
        if (isNullOrHasError(form)) {
            return CompletableFuture.completedFuture(badRequest(jsonError(Code.INVALID_FORM, form)));
        }

        DataTableForm model = form.get();

        return CompletableFuture.supplyAsync(() -> {
            DataTableResultForm result;
            try {
                result = listEntities(model);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonAsDataTable(model, result.recordsTotal, result.recordsFiltered, result.array));
        }, this.context.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        List<Class<?>> groups = getModelCreateGroups();
        Form<M> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest();

        if (isNullOrHasError(form))
            return CompletableFuture.completedFuture(badRequest(jsonError(AController.Code.INVALID_FORM, form)));

        M model = form.get();

        return CompletableFuture.supplyAsync(() -> {
            Long entityId;
            try {
                entityId = createEntity(model);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }

            ObjectNode json = jsonSuccess();
            json.put(Parameter.ID, entityId);
            return ok(json);
        }, this.context.current());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update() {
        List<Class<?>> groups = getModelUpdateGroups();
        Form<M> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest();
        if (isNullOrHasError(form)) {
            return CompletableFuture.completedFuture(badRequest(jsonError(AController.Code.INVALID_FORM, form)));
        }

        M model = form.get();

        return CompletableFuture.supplyAsync(() -> {
            Long entityId;
            try {
                entityId = updateEntity(model);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }

            ObjectNode json = jsonSuccess();
            json.put(Parameter.ID, entityId);
            return ok(json);
        }, this.context.current());
    }

    public CompletionStage<Result> detail(long id) {
        return CompletableFuture.supplyAsync(() -> {
            M model;
            try {
                model = detailEntity(id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }

            ObjectNode json = jsonSuccess();
            json.set(Parameter.ENTITY, Json.toJson(model));
            return ok(json);
        }, this.context.current());
    }

    public CompletionStage<Result> block(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                blockEntity(id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> unblock(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                unblockEntity(id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> remove(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                removeEntity(id);
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(jsonSuccess());
        }, this.context.current());
    }

    public CompletionStage<Result> search(String value) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode json;
            try {
                json = Json.toJson(searchEntities(value));
            } catch (CoreException e) {
                return badRequest(jsonError(e.getCode(), e.getMessage()));
            }
            return ok(json);
        }, this.context.current());
    }

}
