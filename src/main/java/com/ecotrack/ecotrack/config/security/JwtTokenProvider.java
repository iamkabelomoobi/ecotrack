package com.ecotrack.ecotrack.config.security;

import io.jsonwebtoken.*;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.security.Key;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

@Component

public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Key signingKey;

    private final long expirationTimeMs;

    public JwtTokenProvider(

            @Value("${jwt.secret}") String secretKey,

            @Value("${jwt.expiration-time-ms:86400000}") long expirationTimeMs) {

        if (secretKey == null || secretKey.length() < 32) {

            throw new IllegalArgumentException("JWT secret must be ≥32 characters");

        }

        this.signingKey = new SecretKeySpec(secretKey.getBytes(),

                SignatureAlgorithm.HS256.getJcaName());

        this.expirationTimeMs = expirationTimeMs;

    }

    public String generateToken(Long userId, String role) {

        return Jwts.builder()

                .setSubject(userId.toString())

                .claim("role", role)

                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))

                .signWith(signingKey, SignatureAlgorithm.HS256)

                .compact();

    }

    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()

                    .setSigningKey(signingKey)

                    .build()

                    .parseClaimsJws(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {

            log.warn("Invalid JWT token: {}", e.getMessage());

            return false;

        }

    }

    public Long getUserIdFromToken(String token) throws JwtException {

        try {

            String subject = Jwts.parserBuilder()

                    .setSigningKey(signingKey)

                    .build()

                    .parseClaimsJws(token)

                    .getBody()

                    .getSubject();

            return Long.parseLong(subject);

        } catch (ExpiredJwtException ex) {

            log.warn("JWT token expired: {}", ex.getMessage());

            throw ex;

        } catch (MalformedJwtException ex) {

            log.warn("Invalid JWT token: {}", ex.getMessage());

            throw ex;

        } catch (NumberFormatException ex) {

            log.warn("Invalid user ID in token: {}", ex.getMessage());

            throw new MalformedJwtException("Invalid user ID format in token");

        }

    }

    public String getRoleFromToken(String token) throws JwtException {

        try {

            return Jwts.parserBuilder()

                    .setSigningKey(signingKey)

                    .build()

                    .parseClaimsJws(token)

                    .getBody()

                    .get("role", String.class);

        } catch (ExpiredJwtException ex) {

            log.warn("JWT token expired: {}", ex.getMessage());

            throw ex;

        } catch (MalformedJwtException ex) {

            log.warn("Invalid JWT token: {}", ex.getMessage());

            throw ex;

        }

    }

    public Claims getAllClaimsFromToken(String token) throws JwtException {

        try {

            return Jwts.parserBuilder()

                    .setSigningKey(signingKey)

                    .build()

                    .parseClaimsJws(token)

                    .getBody();

        } catch (ExpiredJwtException ex) {

            log.warn("JWT token expired: {}", ex.getMessage());

            throw ex;

        } catch (MalformedJwtException ex) {

            log.warn("Invalid JWT token: {}", ex.getMessage());

            throw ex;

        }

    }

}