package com.efigueredo.service_gateway.infra.filter;

import com.efigueredo.service_gateway.infra.filter.exception.GatewayException;
import com.efigueredo.service_gateway.infra.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AutenticacaoFilter extends AbstractGatewayFilterFactory<AutenticacaoFilter.Config> {

    @Autowired
    private RouteValidation routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AutenticacaoFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if(this.endPointSolicitadoNaoERestrito(exchange)) {
                this.verificarSeTokenExisteNaRequisicao(exchange);
                String jwtToken = this.obterTokenAutenticacao(exchange);
                this.verificarSeTokenENulo(jwtToken);
                this.verificarSeTokenPossuiOPrefixoEsperado(jwtToken);
                jwtToken = jwtToken.substring(7);
                this.validarToken(jwtToken);
            }

            return chain.filter(exchange);
        });
    }

    private void validarToken(String jwtToken) {
        try {
            this.jwtUtil.validarToken(jwtToken);
        } catch (SignatureException e) {
            throw new GatewayException("Nao autenticado", "O token enviado não é valido", "", "403");
        } catch (ExpiredJwtException e) {
            throw new GatewayException("Nao autenticado", "O token enviado esta expirado", "", "401");
        }
    }

    private void verificarSeTokenENulo(String jwtToken) {
        if(jwtToken == null || jwtToken.isBlank()) {
            throw new GatewayException("Erro token JWT", "O token enviado e nulo", "", "401");
        }
    }

    private void verificarSeTokenPossuiOPrefixoEsperado(String jwtToken) {
        if(!jwtToken.startsWith("Bearer ")) {
            throw new GatewayException("Erro token JWT", "O token enviado nao contem o prefixo 'Bearer '", "", "400");
        }
    }

    private void verificarSeTokenExisteNaRequisicao(ServerWebExchange exchange) {
        if(!exchange.getRequest().getHeaders().containsKey("AUTHORIZATION")) {
            throw new GatewayException("Requisição incompleta", "Nao contem token de autorizacao", "", "401");
        }
    }

    private String obterTokenAutenticacao(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
    }

    private boolean endPointSolicitadoNaoERestrito(ServerWebExchange exchange) {
        return this.routeValidator.isSecured.test(exchange.getRequest());
    }

    public static class Config {

    }

}
