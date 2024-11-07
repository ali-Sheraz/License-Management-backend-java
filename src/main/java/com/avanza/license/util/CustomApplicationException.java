package com.avanza.license.util;

public class CustomApplicationException extends RuntimeException {
    private final String errorMessage;

    public CustomApplicationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

