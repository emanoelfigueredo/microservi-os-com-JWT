package com.efigueredo.service_gateway.infra.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidation {

    public static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/identidade/registrar",
            "/identidade/validar",
            "/identidade/autenticar"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> OPEN_API_ENDPOINTS
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
