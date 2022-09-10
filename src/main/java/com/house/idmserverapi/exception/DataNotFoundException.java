package com.house.idmserverapi.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(final String message) {
        super(message);
    }
}

