package com.example.hotelbooking.handler.exception;

import java.io.Serial;

public class InvalidRequestException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4887668470579634741L;

    public InvalidRequestException(String message) {
        super(message);
    }
}
