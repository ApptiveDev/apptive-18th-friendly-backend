package apptive.team1.friendly.global.error;

import apptive.team1.friendly.domain.post.exception.AccessDeniedException;
import apptive.team1.friendly.domain.post.exception.DuplicatedEmailException;
import apptive.team1.friendly.global.error.exception.CustomException;
import apptive.team1.friendly.global.error.exception.Exception400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("ExceptionHandler catch CustomException: {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("ExceptionHandler catch AccessDeniedException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .error(HttpStatus.FORBIDDEN.name())
                        .code("ACCESS_DENIED")
                        .message(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(value = {Exception400.class})
    protected ResponseEntity<ErrorResponse> handleException400(Exception400 e) {
        log.error("ExceptionHandler catch Exception400: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.name())
                        .code("BAD_REQUEST")
                        .message(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(value = {DuplicatedEmailException.class})
    protected ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException e) {
        log.error("ExceptionHandler catch DuplicatedEmailException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.name())
                        .code("DUPLICATED_EMAIL")
                        .message(e.getMessage())
                        .build()
                );
    }
}