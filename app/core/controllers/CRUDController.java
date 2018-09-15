package core.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import core.exceptions.CoreException;
import core.forms.binders.UpdateForm;
import core.forms.validations.CreateGroup;
import core.forms.validations.UpdateGroup;
import io.ebean.Model;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.inject.Inject;
import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class CRUDController<Entity extends Model, EntityForm extends UpdateForm> extends ListController<Entity, EntityForm> {

    private static final String TAG = "CRUDController";

    static final class Parameter {
        static final String ID = "id";
    }

    @Inject
    HttpExecutionContext context;

    public CRUDController(FormFactory formFactory) {
        super(formFactory);
    }

    protected List<Class<?>> getModelCreateGroups() {
        return Arrays.asList(Default.class, CreateGroup.class);
    }

    protected List<Class<?>> getModelUpdateGroups() {
        return Arrays.asList(Default.class, UpdateGroup.class);
    }

    protected abstract Class<EntityForm> getModelClass();
    protected abstract Long createEntity(EntityForm model) throws CoreException;
    protected abstract Long updateEntity(EntityForm model) throws CoreException;

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        List<Class<?>> groups = getModelCreateGroups();
        Form<EntityForm> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest();

        if (isNullOrHasError(form))
            return CompletableFuture.completedFuture(badRequest(jsonError(AController.Code.INVALID_FORM, form)));

        EntityForm model = form.get();

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
        Form<EntityForm> form = this.formFactory.form(getModelClass(), groups.toArray(new Class[groups.size()])).bindFromRequest();
        if (isNullOrHasError(form)) {
            return CompletableFuture.completedFuture(badRequest(jsonError(AController.Code.INVALID_FORM, form)));
        }

        EntityForm model = form.get();

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

}
