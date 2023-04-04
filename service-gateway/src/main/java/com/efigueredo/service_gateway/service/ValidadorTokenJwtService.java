package com.efigueredo.service_gateway.service;

import com.efigueredo.service_gateway.infra.exception.GatewayException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class ValidadorTokenJwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validarToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(this.getSignKey()).build().parseClaimsJws(jwtToken);
        } catch (SignatureException e) {
            throw new GatewayException("Nao autenticado", "O token enviado não é valido", "", "403");
        } catch (ExpiredJwtException e) {
            throw new GatewayException("Nao autenticado", "O token enviado esta expirado", "", "401");
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
