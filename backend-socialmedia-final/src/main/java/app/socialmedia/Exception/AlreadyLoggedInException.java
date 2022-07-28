package app.socialmedia.Exception;

public class AlreadyLoggedInException extends  RuntimeException{
    private static final long serialVersionUID = -2988421478927589342L;

    public AlreadyLoggedInException() {
    }

    public AlreadyLoggedInException(String message) {
        super(message);
    }
}
