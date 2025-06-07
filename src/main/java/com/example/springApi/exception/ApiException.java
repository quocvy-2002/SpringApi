package com.example.springApi.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}