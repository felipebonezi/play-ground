package core.forms.binders;

import core.forms.validations.UpdateGroup;
import play.data.validation.Constraints;

public abstract class UpdateForm {
  
  @Constraints.Min(value = 1, groups = UpdateGroup.class)
  @Constraints.Required(groups = UpdateGroup.class)
  private Long id;
  
  protected UpdateForm() {
  }
  
  protected UpdateForm(Long id) {
    this.id = id;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
}
