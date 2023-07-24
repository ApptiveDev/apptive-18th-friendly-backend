package apptive.team1.friendly.domain.post.exception;

public class InvalidHashTagException extends RuntimeException{

    public InvalidHashTagException() {
    }

    public InvalidHashTagException(String message) {
        super(message);
    }

    public InvalidHashTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHashTagException(Throwable cause) {
        super(cause);
    }

    public InvalidHashTagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
