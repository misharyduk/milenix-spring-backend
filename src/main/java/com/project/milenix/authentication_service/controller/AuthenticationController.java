package com.project.milenix.authentication_service.controller;

import com.google.common.base.Strings;
import com.project.milenix.authentication_service.dto.EmailTokenVerificationDto;
import com.project.milenix.authentication_service.dto.UsernamePasswordDto;
import com.project.milenix.authentication_service.service.EmailSenderService;
import com.project.milenix.authentication_service.service.EmailVerificationService;
import com.project.milenix.file.service.UserFileStorageService;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.exception.UsernameNotUniqueException;
import com.project.milenix.user_service.user.dto.UserRequestDto;
import com.project.milenix.user_service.user.service.UserService;
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
import org.springframework.web.multipart.MultipartFile;

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
  private final UserService userService;
  private final UserFileStorageService fileStorageService;
  private final EmailSenderService emailSenderService;
  private final EmailVerificationService emailVerificationService;

  @PostMapping("register")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> register(@Valid UserRequestDto user,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    HttpServletRequest request) throws EmailNotUniqueException, UsernameNotUniqueException, CustomUserException {
    Integer userId = userService.registerUser(user, image.getOriginalFilename());
    String fileName = fileStorageService.storeFile(userId, image);

    EmailTokenVerificationDto verificationToken = emailVerificationService.generateToken(userId);
    emailSenderService.sendEmail(user.getUsername(), user.getEmail(),
            verificationToken.getExpectedToken(), request.getContextPath());
    emailVerificationService.saveToken(verificationToken);

    return ResponseEntity.status(HttpStatus.CREATED).body(userId);
  }

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

  @GetMapping("mail/verification")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> verifyEmail(@RequestParam("token") String token){
    boolean isTokenVerified = emailVerificationService.verifyToken(token);
    if(!isTokenVerified)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token cannot be verified"); // TODO use error format

    return ResponseEntity.ok("User account has been activated");
  }
}
