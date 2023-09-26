package apptive.team1.friendly.domain.post.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
    }

    public NotFoundCommentException(String message) {
        super(message);
    }

    public NotFoundCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCommentException(Throwable cause) {
        super(cause);
    }

    public NotFoundCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
