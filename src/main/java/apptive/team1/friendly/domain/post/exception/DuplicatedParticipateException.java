package apptive.team1.friendly.domain.post.exception;

public class DuplicatedParticipateException extends RuntimeException {
    public DuplicatedParticipateException() {
    }

    public DuplicatedParticipateException(String message) {
        super(message);
    }

    public DuplicatedParticipateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedParticipateException(Throwable cause) {
        super(cause);
    }

    public DuplicatedParticipateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
