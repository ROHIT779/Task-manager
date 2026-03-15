package com.apps.task_manager.dto;

import java.io.Serializable;

public class ErrorResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int statusCode;
    private String message;

    public ErrorResponseDTO(){}

    public ErrorResponseDTO(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
