package moja.refrigerator.exception.handler;

import moja.refrigerator.exception.user.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
