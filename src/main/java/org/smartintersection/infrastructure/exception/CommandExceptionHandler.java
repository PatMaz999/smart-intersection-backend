package org.smartintersection.infrastructure.exception;

import lombok.NonNull;
import org.smartintersection.domain.exception.IllegalTurnException;
import org.smartintersection.domain.exception.InvalidDirectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CommandExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(message));
    }

    @ExceptionHandler(IllegalTurnException.class)
    public ResponseEntity<@NonNull ErrorResponseDto> handleIllegalTurnException(IllegalTurnException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidDirectionException.class)
    public ResponseEntity<@NonNull ErrorResponseDto> handleInvalidDirectionException(InvalidDirectionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}
