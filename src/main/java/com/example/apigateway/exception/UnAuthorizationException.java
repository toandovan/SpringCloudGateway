package com.example.apigateway.exception;

public class UnAuthorizationException extends Exception{
    public UnAuthorizationException(String message) {
        super(message);
    }
}
