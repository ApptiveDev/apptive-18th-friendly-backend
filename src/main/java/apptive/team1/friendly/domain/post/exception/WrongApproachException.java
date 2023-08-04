package apptive.team1.friendly.domain.post.exception;

public class WrongApproachException extends RuntimeException{
    public WrongApproachException() {
    }

    public WrongApproachException(String message) {
        super(message);
    }

    public WrongApproachException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongApproachException(Throwable cause) {
        super(cause);
    }

    public WrongApproachException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
