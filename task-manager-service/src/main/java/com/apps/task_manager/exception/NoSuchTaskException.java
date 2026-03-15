package com.apps.task_manager.exception;

public class NoSuchTaskException extends RuntimeException {

    private String message;

    public NoSuchTaskException(){}
    public NoSuchTaskException(String message) {
        super(message);
        this.message = message;
    }
}
