package core.exceptions;

/** Basic exception. */
public class CoreException extends Exception {
  
  private final int code;
  
  public CoreException(int code) {
    super();
    this.code = code;
  }
  
  public CoreException(int code, String message) {
    super(message);
    this.code = code;
  }
  
  public int getCode() {
    return code;
  }
  
}
