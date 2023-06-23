package com.project.milenix.authentication_service.controller;

import com.project.milenix.authentication_service.dto.UsernamePasswordDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

//  private final AuthenticationService service;

//  @PostMapping("register")
//  @ResponseStatus(HttpStatus.CREATED)
//  public AuthenticationResponse register(@RequestBody @Valid UserRequestDto request) throws EmailNotUniqueException {
//    return service.register(request);
//  }

  @PostMapping("login")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> authenticate(@RequestBody @Valid UsernamePasswordDto request){
    return ResponseEntity.ok().body("ok");
  }

}
