//package com.project.milenix.authentication_service.service;
//
//import com.project.milenix.authentication_service.dto.AuthenticationRequestDto;
//import com.project.milenix.authentication_service.dto.AuthenticationResponse;
//import com.project.milenix.authentication_service.model.Role;
//import com.project.milenix.user_service.exception.EmailNotUniqueException;
//import com.project.milenix.user_service.user.controller.UserController;
//import com.project.milenix.user_service.user.controller.UserDevController;
//import com.project.milenix.user_service.user.dto.UserAuthResponseDto;
//import com.project.milenix.user_service.user.dto.UserRequestDto;
//import com.project.milenix.authentication_service.model.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//  private final UserController userController;
//  private final UserDevController userDevController;
//  private final PasswordEncoder passwordEncoder;
//  private final JwtService jwtService;
//  private final AuthenticationManager authenticationManager;
//
//  public AuthenticationResponse register(UserRequestDto request) throws EmailNotUniqueException {
//    String encodePassword = passwordEncoder.encode(request.getPassword());
//    UserAuthResponseDto userResponse = UserAuthResponseDto.builder()
//            .email(request.getEmail())
//            .firstName(request.getFirstName())
//            .lastName(request.getLastName())
//            .password(encodePassword)
//            .role(Role.USER.name())
//            .build();
//
//    Integer userId = userDevController.registerUser(UserRequestDto.builder().firstName(
//                    userResponse.getFirstName()).lastName(userResponse.getLastName())
//            .email(userResponse.getEmail()).password(userResponse.getPassword())
//            .build());
//
//    UserDetailsImpl userDetails = getUserDetails(userResponse);
//
//    String jwtToken = jwtService.generateToken(userDetails);
//    return AuthenticationResponse.builder()
//            .id(userId)
//        .token(jwtToken)
//        .build();
//  }
//
//  public AuthenticationResponse authenticate(AuthenticationRequestDto request) {
//
//    authenticationManager.authenticate(
//      new UsernamePasswordAuthenticationToken(
//          request.getEmail(),
//          request.getPassword()
//      )
//    );
//
//    UserAuthResponseDto userFromDb = userDevController.getByEmailToAuthenticate(request.getEmail());
//
//    if(userFromDb.getId() == 0)
//      throw new UsernameNotFoundException("User with email " + request.getEmail() + " is not found");
//
//    UserDetailsImpl userDetails = getUserDetails(userFromDb);
//
//    var jwtToken = jwtService.generateToken(userDetails);
//    return AuthenticationResponse.builder()
//            .id(userFromDb.getId())
//        .token(jwtToken)
//        .build();
//  }
//
//  private UserDetailsImpl getUserDetails(UserAuthResponseDto request) {
//    return UserDetailsImpl.builder()
//        .username(request.getEmail())
//        .password(request.getPassword())
//        .role(Role.valueOf(request.getRole().toUpperCase()))
//        .build();
//  }
//}
