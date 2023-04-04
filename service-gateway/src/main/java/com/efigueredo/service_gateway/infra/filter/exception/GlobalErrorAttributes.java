package com.efigueredo.service_gateway.infra.filter.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        Throwable error = getError(request);
        if(error instanceof GatewayException) {
            GatewayException erro = (GatewayException) error;
            errorMap.put("title", erro.getTitle());
            errorMap.put("detail", erro.getDetail());
            errorMap.put("type", erro.getType());
            errorMap.put("status", erro.getStatus());
        }
        return errorMap;
    }
}
