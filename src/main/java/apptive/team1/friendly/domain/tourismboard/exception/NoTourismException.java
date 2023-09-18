package apptive.team1.friendly.domain.tourismboard.exception;

public class NoTourismException extends RuntimeException{
    public NoTourismException() {
    }

    public NoTourismException(String message) {
        super(message);
    }

    public NoTourismException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTourismException(Throwable cause) {
        super(cause);
    }

    public NoTourismException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
