package com.efigueredo.service_identidade.infra.conf.filter;

import com.efigueredo.service_identidade.infra.conf.exception.DtoErro;
import com.efigueredo.service_identidade.infra.conf.exception.HandlerException;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerException handlerException;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (IdentityException e) {
            Map<String, String> body = Map.of(
                        "title", e.getTitle(),
                        "detail", e.getDetail(),
                        "type", e.getType(),
                        "status", e.getStatus()
                        );

            String json = new ObjectMapper().writeValueAsString(body);

            response.setStatus(Integer.parseInt(e.getStatus()));
            response.getWriter().write(json);
        } catch (BadCredentialsException e) {
            System.out.println("catou");
            response.getWriter().write("Credenciais inválidas");
        } catch (Exception e) {
            this.handlerException.tratarExcecoesGenerias(e);

        }
    }



}
