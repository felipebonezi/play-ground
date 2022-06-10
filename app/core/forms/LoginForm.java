package core.forms;

import play.data.validation.Constraints;

public class LoginForm {
  
  @Constraints.Email
  @Constraints.Required
  public String email;
  
  @Constraints.MinLength(6)
  @Constraints.Required
  public String password;
  
}
