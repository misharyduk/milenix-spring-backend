package com.project.milenix.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiException {
    private final String error;
    private final HttpStatus httpStatus;
}
