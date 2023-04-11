package com.efigueredo.service_gateway.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.efigueredo.service_gateway.infra.exception.GatewayException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class ValidadorTokenJwtService {

    @Value("${secret.key}")
    private String SECRET;

    public void validarToken(String jwtToken) throws GatewayException {
        try {
            var algoritmo = Algorithm.HMAC256(this.SECRET);
            JWT.require(algoritmo)
                    .build()
                    .verify(jwtToken);
        } catch (SignatureVerificationException ex) {
            throw new GatewayException("Falha na autenticação", "Token JWT inválido", "", "403");
        } catch (ExpiredJwtException ex) {
            throw new GatewayException("Falha na autenticação", "Token JWT expirado", "", "401");
        }
    }

}
