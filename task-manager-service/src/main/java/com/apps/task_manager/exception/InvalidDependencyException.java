package com.apps.task_manager.exception;

public class InvalidDependencyException extends RuntimeException {

    private String message;

    public InvalidDependencyException(){}
    public InvalidDependencyException(String message) {
        super(message);
        this.message = message;
    }
}
