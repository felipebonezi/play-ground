package core.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.exceptions.CoreException;
import core.forms.DataTableResultForm;
import core.forms.Select2Form;
import core.forms.binders.UpdateForm;
import core.forms.binders.datatable.DataTableForm;
import io.ebean.Model;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class ListController<Entity extends Model, EntityForm extends UpdateForm> extends AController {

    private static final String TAG = "ListController";

    static final class Parameter {
        static final String ID = "id";
        static final String ENTITY = "entity";
    }

    @Inject
    public HttpExecutionContext context;

    public ListController(FormFactory formFactory) {
        super(formFactory);
    }

    protected abstract DataTableResultForm listEntities(DataTableForm model) throws CoreException;
    protected abstract EntityForm detailEntity(long entityId) throws CoreException;

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

    public CompletionStage<Result> detail(long id) {
        return CompletableFuture.supplyAsync(() -> {
            EntityForm model;
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
