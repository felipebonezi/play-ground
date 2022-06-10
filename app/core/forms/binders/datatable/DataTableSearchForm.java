package core.forms.binders.datatable;

public class DataTableSearchForm {
  
  private boolean regex;
  private String  value;
  
  public boolean isRegex() {
    return regex;
  }
  
  public void setRegex(boolean regex) {
    this.regex = regex;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
}
