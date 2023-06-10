package com.project.milenix.authentication_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  private static final String SECRET_KEY = "28482B4B6250655368566D597133743677397A24432646294A404E635166546A";


  //               TOKEN GENERATION
  public String generateToken(UserDetails userDetails){
    return generateToken(userDetails, new HashMap<>());
  }

  public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims){
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSignInKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  //              TOKEN VALIDATION
  public boolean isTokenValid(String jwtToken, UserDetails userDetails){
    final String username = extractUsername(jwtToken);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
  }

  //              TOKEN PARSING
  private Claims extractAllClaims(String jwtToken){
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(jwtToken)
        .getBody();
  }

  private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(jwtToken);
    return claimsResolver.apply(claims);
  }

  //             EXTRACT CLAIMS
  public String extractUsername(String jwtToken){
    return extractClaim(jwtToken, Claims::getSubject);
  }

  private Date extractExpiration(String jwtToken) {
    return extractClaim(jwtToken, Claims::getExpiration);
  }

  private boolean isTokenExpired(String jwtToken){
    return extractExpiration(jwtToken).before(new Date());
  }
}
