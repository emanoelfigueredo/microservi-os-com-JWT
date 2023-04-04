package com.efigueredo.service_gateway.service;

import com.efigueredo.service_gateway.infra.exception.GatewayException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class VerificadorTokenJwtService {

    public void verificarSeTokenENulo(String jwtToken) {
        if(jwtToken == null || jwtToken.isBlank()) {
            throw new GatewayException("Erro token JWT", "O token enviado e nulo", "", "401");
        }
    }

    public void verificarSeTokenPossuiOPrefixoEsperado(String jwtToken) {
        if(!jwtToken.startsWith("Bearer ")) {
            throw new GatewayException("Erro token JWT", "O token enviado nao contem o prefixo 'Bearer '", "", "400");
        }
    }

    public void verificarSeTokenExisteNaRequisicao(ServerWebExchange exchange) {
        if(!exchange.getRequest().getHeaders().containsKey("AUTHORIZATION")) {
            throw new GatewayException("Requisição incompleta", "Nao contem token de autorizacao", "", "401");
        }
    }

}
