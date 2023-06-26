package com.project.milenix.authentication_service.controller;

import com.google.common.base.Strings;
import com.project.milenix.authentication_service.dto.UsernamePasswordDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final UserDetailsService userDetailsService;

  @PostMapping("token/refresh")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken, HttpServletRequest request){
    if(Strings.isNullOrEmpty(refreshToken)){
      throw new RuntimeException("No token available");
    }

    SecretKey key = Keys.hmacShaKeyFor("securitysecuritysecuritysecuritysecurity".getBytes());
    Jws<Claims> claimsJws = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(refreshToken);

    Claims body = claimsJws.getBody();
    String username = body.getSubject();

    boolean isExpired = body.getExpiration().before(new Date());
    if(isExpired){
      return ResponseEntity.status(FORBIDDEN).body("Refresh token is expired");
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if(userDetails != null){
      String jwtAccessToken = Jwts.builder()
              .signWith(key)
              .setIssuer(request.getContextPath())
              .setSubject(username)
              .claim("authorities", userDetails.getAuthorities())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3)))
              .compact();

      Map<String, String> tokens = new HashMap<>();
      tokens.put("access_token", jwtAccessToken);
      tokens.put("refresh_token", refreshToken);
      return ResponseEntity.ok(tokens);
    } else {
      return ResponseEntity.status(FORBIDDEN).body("User is not found");
    }
  }

}
