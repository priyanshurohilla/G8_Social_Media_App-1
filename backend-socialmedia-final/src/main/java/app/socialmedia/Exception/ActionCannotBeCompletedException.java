package app.socialmedia.Exception;

public class ActionCannotBeCompletedException extends RuntimeException{
    private static final long serialVersionUID= -2695962140786680195L;

    public ActionCannotBeCompletedException() {
    }

    public ActionCannotBeCompletedException(String message) {
        super(message);
    }
}
