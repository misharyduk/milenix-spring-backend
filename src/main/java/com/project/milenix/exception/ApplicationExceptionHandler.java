package com.project.milenix.exception;

import com.project.milenix.article_service.exception.ArticleException;
import com.project.milenix.category_service.exception.CategoryException;
import com.project.milenix.category_service.exception.NameNotUniqueException;
import com.project.milenix.exception.dto.ApiException;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, String> handleInvalidArgument(BindException ex){
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArticleException.class)
    public ResponseEntity<ApiException> handleArticleException(ArticleException ex){

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                ex.getMessage(),
                badRequest
        );


        return new ResponseEntity<>(apiException, badRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotUniqueException.class)
    public Map<String, String> handleInvalidEmail(EmailNotUniqueException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("email", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ApiException> handleUserException(CustomUserException ex){

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                ex.getMessage(),
                badRequest
        );


        return new ResponseEntity<>(apiException, badRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NameNotUniqueException.class)
    public Map<String, String> handleInvalidName(NameNotUniqueException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("name", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiException> handleNotFoundException(Exception ex){
        ApiException apiException = new ApiException(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ApiException> handleUserException(CategoryException ex){

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                ex.getMessage(),
                badRequest
        );


        return new ResponseEntity<>(apiException, badRequest);
    }
}
