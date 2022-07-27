package app.socialmedia.Exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3318616007881236273L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
