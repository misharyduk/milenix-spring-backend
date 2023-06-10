package com.project.milenix.user_service.exception;

public class EmailNotUniqueException extends Exception{
    public EmailNotUniqueException() {
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }
}
