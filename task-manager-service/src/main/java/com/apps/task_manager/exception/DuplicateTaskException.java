package com.apps.task_manager.exception;

public class DuplicateTaskException extends RuntimeException {

    private String message;

    public DuplicateTaskException(){}
    public DuplicateTaskException(String message) {
        super(message);
        this.message = message;
    }
}
