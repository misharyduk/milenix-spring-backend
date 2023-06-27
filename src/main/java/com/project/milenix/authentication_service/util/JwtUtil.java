package com.project.milenix.authentication_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JwtUtil {
    public static String getUsernameFromToken(HttpServletRequest request){ // TODO refactor method with Function
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = tokenHeader.replace("Bearer ", "");
        return getBodyClaims(jwtToken).getSubject();
    }

    public static String getEmailFromToken(HttpServletRequest request){ // TODO refactor method with Function
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = tokenHeader.replace("Bearer ", "");
        return getBodyClaims(jwtToken).get("email", String.class);
    }

    public static Integer getIdFromToken(HttpServletRequest request){ // TODO refactor method with Function
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = tokenHeader.replace("Bearer ", "");
        return getBodyClaims(jwtToken).get("id", Integer.class);
    }

    private static Claims getBodyClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("securitysecuritysecuritysecuritysecurity".getBytes()))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}
