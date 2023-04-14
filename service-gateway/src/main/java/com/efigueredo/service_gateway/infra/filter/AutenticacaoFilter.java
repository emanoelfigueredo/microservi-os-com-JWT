package com.efigueredo.service_gateway.infra.filter;

import com.efigueredo.service_gateway.service.ValidadorTokenJwtService;
import com.efigueredo.service_gateway.service.VerificadorTokenJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Service
public class AutenticacaoFilter extends AbstractGatewayFilterFactory<AutenticacaoFilter.Config> {

    @Autowired
    private RouteValidation routeValidator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ValidadorTokenJwtService validadorTokenJwtService;

    @Autowired
    private VerificadorTokenJwtService verificadorToken;

    public AutenticacaoFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(this.endPointSolicitadoNaoERestrito(exchange)) {
                this.verificadorToken.verificarSeTokenExisteNaRequisicao(exchange);
                String jwtToken = this.obterTokenAutenticacao(exchange);
                this.verificadorToken.verificarSeTokenENulo(jwtToken);
                this.verificadorToken.verificarSeTokenPossuiOPrefixoEsperado(jwtToken);
                jwtToken = jwtToken.substring(7);
                this.validadorTokenJwtService.validarToken(jwtToken);
                this.restTemplate.getForEntity("http://localhost:8082/usuarios/token/validar?token=" + jwtToken, String.class);
            }
            return chain.filter(exchange);
        });
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
