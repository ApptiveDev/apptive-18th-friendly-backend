package apptive.team1.friendly.domain.post.exception;

public class InvalidApproachException extends RuntimeException{
    public InvalidApproachException() {
    }

    public InvalidApproachException(String message) {
        super(message);
    }

    public InvalidApproachException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidApproachException(Throwable cause) {
        super(cause);
    }

    public InvalidApproachException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
