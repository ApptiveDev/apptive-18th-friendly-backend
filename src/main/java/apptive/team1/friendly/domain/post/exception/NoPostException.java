package apptive.team1.friendly.domain.post.exception;

public class NoPostException extends RuntimeException {
    public NoPostException() {
    }

    public NoPostException(String message) {
        super(message);
    }

    public NoPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPostException(Throwable cause) {
        super(cause);
    }

    public NoPostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
