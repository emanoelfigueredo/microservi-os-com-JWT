package com.efigueredo.service_identidade.service;

import java.security.Key;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenJwtService {

    @Value("${secret.key}")
    private String SECRET;

    public void validarToken(String jwtToken) {
        var algoritmo = Algorithm.HMAC256(this.SECRET);
        JWT.require(algoritmo)
                .build()
                .verify(jwtToken);
    }

    public TokenJwt gerarToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return this.criarToken(claims, username);
    }

    private TokenJwt criarToken(Map<String, Object> claims, String username) {
        Date momentoAtual = new Date(System.currentTimeMillis());
        Date momentoExpiracao = new Date(System.currentTimeMillis() + 1000 * 60 * 30); // 30 minutos no futuro
        var algoritmo = Algorithm.HMAC256(this.SECRET);
        String token = JWT.create()
                .withSubject(username)
                .withIssuedAt(momentoAtual)
                .withExpiresAt(momentoExpiracao)
                .sign(algoritmo);
        return new TokenJwt(token, momentoAtual, momentoExpiracao);
    }

    public String obterSubject(String tokenJwt) {
        var algoritmo = Algorithm.HMAC256(this.SECRET);
        return JWT.require(algoritmo)
                .build()
                .verify(tokenJwt)
                .getSubject();
    }
}
