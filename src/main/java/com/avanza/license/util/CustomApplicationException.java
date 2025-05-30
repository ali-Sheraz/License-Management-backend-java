package com.avanza.license.util;

public class CustomApplicationException extends RuntimeException {
    private final String errorMessage;
    private final int statusCode;

    public CustomApplicationException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public int getstatusCode() {
        return statusCode;
    }
}

