package com.jorge.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    // Chave secreta vinda do application.properties (mínimo de 32 caracteres para HS256)
    @Value("${jwt.secret}")
    private String secretKey;

    // Converte a secretKey em uma SecretKey válida para assinatura e validação
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Gera um token JWT com o e-mail/usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }

    // Extrai as claims do token JWT (padrão JJWT 0.12.x)
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(limparToken(token))
                .getPayload();
    }

    // Extrai o e-mail (subject) do token JWT
    public String extrairEmailToken(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // Valida se o token pertence ao usuário e não está expirado
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extrairEmailToken(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Remove o prefixo "Bearer " e espaços extras caso o token chegue bruto
    private String limparToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token != null ? token.trim() : "";
    }
}