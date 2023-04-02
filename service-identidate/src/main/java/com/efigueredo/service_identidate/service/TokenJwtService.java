package com.efigueredo.service_identidate.service;

import java.security.Key;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenJwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validarToken(String token) {
        Jwts.parserBuilder().setSigningKey(this.getSignKey()).build().parseClaimsJwt(token);
    }

    public TokenJwt gerarToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return this.criarToken(claims, username);
    }

    private TokenJwt criarToken(Map<String, Object> claims, String username) {
        Date momentoAtual = new Date(System.currentTimeMillis());
        Date momentoExpiracao = new Date(System.currentTimeMillis() + 1000 * 60 * 30); // 30 minutos no futuro
        String token = Jwts.builder()
                                .setClaims(claims)
                                .setSubject(username)
                                .setIssuedAt(momentoAtual)
                                .setExpiration(momentoExpiracao)
                                .signWith(this.getSignKey(), SignatureAlgorithm.HS256).compact();
        return new TokenJwt(token, momentoAtual, momentoExpiracao);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
