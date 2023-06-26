package com.project.milenix.user_service.exception;

public class UsernameNotUniqueException extends Exception{
    public UsernameNotUniqueException() {
    }

    public UsernameNotUniqueException(String message) {
        super(message);
    }

    public UsernameNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameNotUniqueException(Throwable cause) {
        super(cause);
    }
}
