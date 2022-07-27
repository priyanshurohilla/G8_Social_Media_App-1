package app.socialmedia.Exception;

public class PostNotFoundException extends  RuntimeException{
    private static final long serialVersionUID = -6081024387502268457L;

    public PostNotFoundException() {
        super();
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
