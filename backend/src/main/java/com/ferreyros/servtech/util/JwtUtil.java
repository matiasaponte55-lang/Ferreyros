package com.ferreyros.servtech.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para generación y validación de tokens JWT 
 */
@Component
public class JwtUtil {

    @Value("${servtech.jwt.secret}")
    private String jwtSecret;

    @Value("${servtech.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        String secret = (this.jwtSecret != null && !this.jwtSecret.trim().isEmpty())
                ? this.jwtSecret
                : "FerreyrosServTech2026SecureKeyWithMin256BitsLengthForHmacSha256Security";
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String username, String rol, String nombres) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        claims.put("nombres", nombres);

        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(fechaExpiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String obtenerRol(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return (String) claims.get("rol");
    }
}
