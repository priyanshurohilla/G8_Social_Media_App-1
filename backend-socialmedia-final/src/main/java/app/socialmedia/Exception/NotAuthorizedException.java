package app.socialmedia.Exception;

public class NotAuthorizedException extends  RuntimeException{
    private static final long serialVersionUID = -4901851343253760689L;

    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
