package core.forms.binders;

import core.forms.validations.UpdateGroup;
import play.data.validation.Constraints;

public abstract class UpdateForm {

    @Constraints.Min(value = 1, groups = UpdateGroup.class)
    @Constraints.Required(groups = UpdateGroup.class)
    public Long id;

    public UpdateForm() {}

    public UpdateForm(Long id) {
        this.id = id;
    }

}
