package com.practice.swmbackend.global.exception;

public class SignUpFailedException extends RuntimeException {
    public SignUpFailedException(String message) {
        super(message);
    }
}
