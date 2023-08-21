package apptive.team1.friendly.domain.post.exception;

public class ExcessOfPeopleException extends RuntimeException{
    public ExcessOfPeopleException() {
    }

    public ExcessOfPeopleException(String message) {
        super(message);
    }

    public ExcessOfPeopleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcessOfPeopleException(Throwable cause) {
        super(cause);
    }

    public ExcessOfPeopleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
