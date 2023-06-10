package com.project.milenix.user_service.exception;

public class CustomUserException extends Exception{

  public CustomUserException() {
    super();
  }

  public CustomUserException(String message) {
    super(message);
  }

  public CustomUserException(String message, Throwable cause) {
    super(message, cause);
  }

  public CustomUserException(Throwable cause) {
    super(cause);
  }
}
