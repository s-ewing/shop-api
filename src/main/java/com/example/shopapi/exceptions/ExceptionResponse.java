package com.example.shopapi.exceptions;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private Object data;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public ExceptionResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
