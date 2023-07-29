package apptive.team1.friendly.domain.curating.exception;

public class CanNotDeleteHeartException extends RuntimeException{
    public CanNotDeleteHeartException() {
    }

    public CanNotDeleteHeartException(String message) {
        super(message);
    }

    public CanNotDeleteHeartException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotDeleteHeartException(Throwable cause) {
        super(cause);
    }

    public CanNotDeleteHeartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
