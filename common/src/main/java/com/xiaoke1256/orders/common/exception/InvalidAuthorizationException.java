package com.xiaoke1256.orders.common.exception;

public class InvalidAuthorizationException extends RuntimeException {
    public InvalidAuthorizationException() {
        super();
    }

    public InvalidAuthorizationException(String message) {
        super(message);
    }
}
