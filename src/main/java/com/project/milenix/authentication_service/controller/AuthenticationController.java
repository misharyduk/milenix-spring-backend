package com.project.milenix.authentication_service.controller;

import com.project.milenix.authentication_service.dto.AuthenticationResponse;
import com.project.milenix.authentication_service.dto.AuthenticationRequestDto;
import com.project.milenix.authentication_service.service.AuthenticationService;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.user.dto.UserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthenticationResponse register(@RequestBody @Valid UserRequestDto request) throws EmailNotUniqueException {
    return service.register(request);
  }

  @PostMapping("authenticate")
  @ResponseStatus(HttpStatus.OK)
  public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequestDto request){
    return service.authenticate(request);
  }

}
