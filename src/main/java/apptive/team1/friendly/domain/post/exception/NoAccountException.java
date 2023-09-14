package apptive.team1.friendly.domain.post.exception;

public class NoAccountException extends RuntimeException {
    public NoAccountException() {
    }

    public NoAccountException(String message) {
        super(message);
    }

    public NoAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAccountException(Throwable cause) {
        super(cause);
    }

    public NoAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
