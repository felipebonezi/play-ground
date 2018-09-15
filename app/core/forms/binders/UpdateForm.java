package core.forms.binders;

import core.forms.validations.UpdateGroup;
import play.data.validation.Constraints;

public abstract class UpdateForm {

    @Constraints.Min(value = 1L, groups = UpdateGroup.class)
    @Constraints.Required(groups = UpdateGroup.class)
    public Long id;

}
