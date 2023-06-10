package com.project.milenix.category_service.exception;

public class CategoryException extends Exception{

  public CategoryException() {
  }

  public CategoryException(String message) {
    super(message);
  }

  public CategoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public CategoryException(Throwable cause) {
    super(cause);
  }
}