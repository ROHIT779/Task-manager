package com.apps.task_manager.exception;

public class InvalidOperationException extends RuntimeException {

    private String message;

    public InvalidOperationException(){}
    public InvalidOperationException(String message) {
        super(message);
        this.message = message;
    }
}
