package apptive.team1.friendly.domain.curating.exception;

public class CanNotPushLikeException extends RuntimeException{
    public CanNotPushLikeException() {
    }

    public CanNotPushLikeException(String message) {
        super(message);
    }

    public CanNotPushLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotPushLikeException(Throwable cause) {
        super(cause);
    }

    public CanNotPushLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
