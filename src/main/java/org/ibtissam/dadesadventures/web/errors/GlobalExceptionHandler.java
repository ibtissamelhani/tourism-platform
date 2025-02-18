package org.ibtissam.dadesadventures.web.errors;

import org.ibtissam.dadesadventures.exception.activity.ActivityNotFoundException;
import org.ibtissam.dadesadventures.exception.category.CategoryAlreadyExistException;
import org.ibtissam.dadesadventures.exception.category.CategoryNotFoundException;
import org.ibtissam.dadesadventures.exception.place.PlaceNotFoundException;
import org.ibtissam.dadesadventures.exception.place.TypeAlreadyExistException;
import org.ibtissam.dadesadventures.exception.place.TypeNotFoundException;
import org.ibtissam.dadesadventures.exception.user.EmailAlreadyExistException;
import org.ibtissam.dadesadventures.exception.user.InvalidCredentialsException;
import org.ibtissam.dadesadventures.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // handel user exceptions
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<String> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handel category exceptions
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<String> handleCategoryAlreadyExistException(CategoryAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // handel place exceptions
    @ExceptionHandler(TypeAlreadyExistException.class)
    public ResponseEntity<String> handleTypeAlreadyExistException(TypeAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TypeNotFoundException.class)
    public ResponseEntity<String> handleTypeNotFoundException(TypeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<String> handlePlaceNotFoundException(PlaceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // handel activity exceptions

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<String> handleActivityNotFoundException(ActivityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }




}
