package com.hospital.ExceptionHandler;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body("Invalid Specialization: " + ex.getMessage());
    }

    //jpa related exception handling
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Database constraint violation: " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + ex.getMessage());
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<String> handleJpaSystemException(JpaSystemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database processing error: " + ex.getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleTransactionSystemException(TransactionSystemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction failed: " + ex.getMessage());
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested Resource Not Found in db: " + ex.getMessage());
    }
    
    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<String> appointmentConflictExceptionHandler(AppointmentConflictException ex){
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointement can't be schedule: " + ex.getMessage());
    }
    
    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<String> resourceAlreadyExistExceptionHandler(ResourceExistException ex){
    	return ResponseEntity.status(HttpStatus.CONFLICT).body("Resource already present in db: " + ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex){
        return ResponseEntity.badRequest().body("Something went wrong: " + ex.getMessage());
    }
    
    
}
