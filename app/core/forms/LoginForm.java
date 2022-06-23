package core.forms;

import play.data.validation.Constraints;

/** Login form. */
public class LoginForm {
  
  @Constraints.Email
  @Constraints.Required
  private String email;
  
  @Constraints.MinLength(6)
  @Constraints.Required
  private String password;
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
}
