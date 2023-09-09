package apptive.team1.friendly.global.error.exception;

public class Exception400 extends RuntimeException {
    public Exception400() {
    }

    public Exception400(String message) {
        super(message);
    }

    public Exception400(String message, Throwable cause) {
        super(message, cause);
    }

    public Exception400(Throwable cause) {
        super(cause);
    }

    public Exception400(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
