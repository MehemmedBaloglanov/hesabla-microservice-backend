package com.hesabla.invoicing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * invoicing-service YALNIZ token DOĞRULAYIR — auth-service-in əksinə
 * burada generateToken() YOXDUR, çünki bu servis heç vaxt token
 * yaratmır (token yalnız login zamanı auth-service-də yaranır). Eyni
 * jwt.secret Config Server-dən gəldiyi üçün imzanı doğrulaya bilirik.
 */
@Component
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
