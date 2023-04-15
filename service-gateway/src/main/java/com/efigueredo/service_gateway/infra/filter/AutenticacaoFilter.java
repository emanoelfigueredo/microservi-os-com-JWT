package com.efigueredo.service_gateway.infra.filter;

import com.efigueredo.service_gateway.service.VerificadorTokenJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Service
public class AutenticacaoFilter extends AbstractGatewayFilterFactory<AutenticacaoFilter.Config> {

    @Value("${ms.identidade.host}")
    private String MS_IDENTIDADE_HOST;

    @Autowired
    private RouteValidation routeValidator;

    @Autowired
    private RestTemplate restTemplate;

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
                ResponseEntity<String> response = this.realizarRequisicaoParaMSIdentidade(jwtToken);
                String roles = response.getBody();
                this.adicionarHeaderRoles(exchange, roles);
            }
            return chain.filter(exchange);
        });
    }

    private void adicionarHeaderRoles(ServerWebExchange exchange, String roles) {
        ServerHttpRequest newHttpRequest = exchange.getRequest().mutate().header("Roles", roles).build();
        exchange.mutate().request(newHttpRequest);
    }

    private ResponseEntity<String> realizarRequisicaoParaMSIdentidade(String jwtToken) {
        HttpEntity<?> request = RequestEntity.get("http://localhost:8082/usuarios/token/validar?token=" + jwtToken).header("AUTHORIZATION", "Bearer " + jwtToken).build();
        return this.restTemplate
                .exchange(
                        "http://localhost:8082/usuarios/token/validar?token=" + jwtToken,
                        HttpMethod.GET,
                        request,
                        String.class
                );
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
