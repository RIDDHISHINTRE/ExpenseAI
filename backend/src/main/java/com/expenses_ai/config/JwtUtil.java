package com.expenses_ai.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String email){

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
             .setSubject(email)
             .setIssuedAt(new Date())
             .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
             .signWith(key , SignatureAlgorithm.HS256)
             .compact();
    }

     public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();         
    }

   
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);                        
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired!");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token!");
        } catch (Exception e) {
            System.out.println("Token error: " + e.getMessage());
        }
        return false;
    }

   
    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)                         
                .build()
                .parseClaimsJws(token)                       
                .getBody();                                  
    }
    
}
