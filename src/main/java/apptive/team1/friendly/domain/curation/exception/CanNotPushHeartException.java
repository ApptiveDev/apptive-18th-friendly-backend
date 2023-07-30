package apptive.team1.friendly.domain.curation.exception;

public class CanNotPushHeartException extends RuntimeException{
    public CanNotPushHeartException() {
    }

    public CanNotPushHeartException(String message) {
        super(message);
    }

    public CanNotPushHeartException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotPushHeartException(Throwable cause) {
        super(cause);
    }

    public CanNotPushHeartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
