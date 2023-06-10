package com.project.milenix.authentication_service.config;

import com.project.milenix.authentication_service.model.Role;
import com.project.milenix.authentication_service.model.UserDetailsImpl;
import com.project.milenix.user_service.user.controller.UserDevController;
import com.project.milenix.user_service.user.dto.UserAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ServiceConfig {

  private final UserDevController userDevController;

  @Bean
  public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public UserDetailsService userDetailsService() {

    return username -> {
      UserAuthResponseDto userAuthResponseDto = userDevController.getByEmailToAuthenticate(username);

      return UserDetailsImpl.builder()
          .username(userAuthResponseDto.getEmail())
          .password(userAuthResponseDto.getPassword())
          .role(Role.valueOf(userAuthResponseDto.getRole().toUpperCase()))
          .build();
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
