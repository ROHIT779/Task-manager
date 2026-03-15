package com.apps.task_manager.exception;

import com.apps.task_manager.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchTaskException.class)
    public ResponseEntity handleNoSuchTaskException(NoSuchTaskException e){
        System.out.println("Inside handler");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateTaskException.class)
    public ResponseEntity handleDuplicateTaskException(DuplicateTaskException e){
        System.out.println("Inside handler");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity handleInvalidOperationException(InvalidOperationException e){
        System.out.println("Inside handler");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidDependencyException.class)
    public ResponseEntity handleInvalidDependencyException(InvalidDependencyException e){
        System.out.println("Inside handler");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e){
        System.out.println("Inside handler");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occured: " + e.getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
