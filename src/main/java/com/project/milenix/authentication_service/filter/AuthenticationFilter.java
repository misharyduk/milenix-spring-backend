package com.project.milenix.authentication_service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.project.milenix.authentication_service.dto.UsernamePasswordDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UsernamePasswordDto usernamePasswordDto = new ObjectMapper().readValue(request.getInputStream(), UsernamePasswordDto.class);
            String username = usernamePasswordDto.getUsername();
            String password = usernamePasswordDto.getPassword();

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authenticate = authenticationManager.authenticate(authentication);

            return authenticate;
        } catch (IOException e){
            throw new SecurityException("Cannot read request");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecretKey key = Keys.hmacShaKeyFor("securitysecuritysecuritysecuritysecurity".getBytes());
        String jwtAccessToken = Jwts.builder()
                .signWith(key)
                .setIssuer(request.getContextPath())
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().plusHours(3).toLocalDate()))
                .compact();

        String jwtRefreshToken = Jwts.builder()
                .signWith(key)
                .setIssuer(request.getContextPath())
                .setSubject(authResult.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDateTime.now().plusDays(14).toLocalDate()))
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtAccessToken);
        tokens.put("refresh_token", jwtRefreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
