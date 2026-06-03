package com.pen.taskmanagement.utilities;

import java.security.Key;
import java.util.Date;
import java.util.Base64.Decoder;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String key;

    private SecretKey secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    @PostConstruct
    public void init(){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey)
            .compact();
                

    }
    public String extractUsername(String token){
        return Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
                
    }
    public Date extractExpiration(String token){

        return Jwts.parser().verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();

    }

    public boolean validateToken(UserDetails userDetails, String token){
        String username = extractUsername(token);
        Date todayDate = new Date(System.currentTimeMillis());

        return (username.equals(userDetails.getUsername()) && extractExpiration(token).after(todayDate));

    }
}
