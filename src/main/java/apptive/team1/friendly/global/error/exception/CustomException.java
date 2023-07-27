package apptive.team1.friendly.global.error.exception;

import apptive.team1.friendly.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }

    public CustomException(Throwable cause, ErrorCode errorCode) {
        super(errorCode.getDetail(), cause);
        this.errorCode = errorCode;
    }
}