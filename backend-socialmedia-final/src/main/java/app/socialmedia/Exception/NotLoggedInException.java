package app.socialmedia.Exception;

public class NotLoggedInException extends RuntimeException{
    private static final long serialVersionUID = -152421931642962418L;

    public NotLoggedInException() {
        super();
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}
