package com.kjh.ticketreserve.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JwtProvider {

    private final SecretKeySpec key;

    private final JwtParser parser;

    private static final long EXPIRATION_TIME = 2 * 60 * 60 * 1000;

    public JwtProvider(SecretKeySpec key) {
        this.key = key;
        this.parser = Jwts.parser().verifyWith(key).build();
    }

    public static JwtProvider create(String jwtSecret) {
        @SuppressWarnings("deprecation") String algorithm = SignatureAlgorithm.HS256.getJcaName();
        SecretKeySpec key = new SecretKeySpec(jwtSecret.getBytes(), algorithm);
        return new JwtProvider(key);
    }

    public String createToken(String subject) {
        Claims claims = Jwts.claims().subject(subject).build();
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(key)
            .compact();
    }

    private Claims parseClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        parseClaims(token);
        return true;
    }
}
