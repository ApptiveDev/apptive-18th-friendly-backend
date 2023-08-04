package apptive.team1.friendly.domain.post.exception;

public class NoEnrollmentException extends RuntimeException{
    public NoEnrollmentException() {
    }

    public NoEnrollmentException(String message) {
        super(message);
    }

    public NoEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEnrollmentException(Throwable cause) {
        super(cause);
    }

    public NoEnrollmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
