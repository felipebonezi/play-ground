package core.forms;

public class Select2Form {
  
  private String id;
  private String text;
  
  public Select2Form() {
  }
  
  public Select2Form(Long id, String text) {
    this(id.toString(), text);
  }
  
  public Select2Form(String id, String text) {
    this.id   = id;
    this.text = text;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
}
