package com.efigueredo.service_identidade.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.efigueredo.service_identidade.service.TokenJwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtil {

    private final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public TokenJwt gerarToken(String username, int tempoExpiracao) {
        Map<String, Object> claims = new HashMap<>();
        return this.criarToken(claims, username, tempoExpiracao);
    }

    private TokenJwt criarToken(Map<String, Object> claims, String username, int tempoExpiracao) {
        Date momentoAtual = new Date(System.currentTimeMillis());
        Date momentoExpiracao = new Date(System.currentTimeMillis() + tempoExpiracao); // 30 minutos no futuro
        var algoritmo = Algorithm.HMAC256(this.SECRET);
        String token = JWT.create()
                .withSubject(username)
                .withIssuedAt(momentoAtual)
                .withExpiresAt(momentoExpiracao)
                .sign(algoritmo);
        return new TokenJwt(token, momentoAtual, momentoExpiracao);
    }

}
