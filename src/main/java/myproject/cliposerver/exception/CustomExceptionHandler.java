package myproject.cliposerver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleApiRequestException(CustomException customException) {
        log.error(customException.getErrorCode().getErrorMessage());
        return ResponseEntity.status(customException.getErrorCode().getHttpStatus()).body(new CustomExceptionDTO(customException.getErrorCode()));
    }
}
