package core.forms;

public class Select2Form {

    public String id;
    public String text;

    public Select2Form() {}

    public Select2Form(Long id, String text) {
        this(id.toString(), text);
    }

    public Select2Form(String id, String text) {
        this.id = id;
        this.text = text;
    }

}
