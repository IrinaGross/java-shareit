package ru.practicum.shareit.exception.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
@SuppressWarnings("unused")
public class DefaultControllerAdvice {
    private static final String ERROR_KEY = "error";

    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class, UnsupportedStatusException.class})
    public ResponseEntity<Object> handleBadRequestException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(getCustomBody(ex));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(getCustomBody(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(getCustomBody(ex), HttpStatus.NOT_FOUND);
    }

    @NonNull
    private static Map<String, String> getCustomBody(@NonNull RuntimeException e) {
        var message = e.getMessage();
        return Map.of(ERROR_KEY, message != null ? message : "");
    }
}
