package org.ibtissam.dadesadventures.web.errors;

import org.ibtissam.dadesadventures.exception.activity.ActivityDeletionException;
import org.ibtissam.dadesadventures.exception.activity.ActivityNotFoundException;
import org.ibtissam.dadesadventures.exception.category.CategoryAlreadyExistException;
import org.ibtissam.dadesadventures.exception.category.CategoryNotFoundException;
import org.ibtissam.dadesadventures.exception.place.PlaceNotFoundException;
import org.ibtissam.dadesadventures.exception.place.TypeAlreadyExistException;
import org.ibtissam.dadesadventures.exception.place.TypeNotFoundException;
import org.ibtissam.dadesadventures.exception.reservation.*;
import org.ibtissam.dadesadventures.exception.review.ReviewNotFoundException;
import org.ibtissam.dadesadventures.exception.user.EmailAlreadyExistException;
import org.ibtissam.dadesadventures.exception.user.GuideIsBusyException;
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
    @ExceptionHandler(GuideIsBusyException.class)
    public ResponseEntity<String> handleGuideIsBusyException(GuideIsBusyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // handel category exceptions
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleCategoryAlreadyExistException(CategoryAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
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

    @ExceptionHandler(ActivityDeletionException.class)
    public ResponseEntity<String> handleActivityDeletionException(ActivityDeletionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // handel reservation exceptions
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFoundException(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<String> handleFailedToSendEmailException(FailedToSendEmailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<String> handlePaymentFailedException(PaymentFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RegistrationClosedException.class)
    public ResponseEntity<String> handleRegistrationClosedException(RegistrationClosedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // handel review exceptions
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
