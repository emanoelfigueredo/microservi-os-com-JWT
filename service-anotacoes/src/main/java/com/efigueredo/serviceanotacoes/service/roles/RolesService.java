package com.efigueredo.serviceanotacoes.service.roles;

import com.efigueredo.serviceanotacoes.infra.handler.exceptions.AnotacaoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class RolesService {

    @Autowired
    private RestTemplate restTemplate;

    public void verificarAutorizacaoDoUsuario(HttpServletRequest request, Roles role) {
        String tokenJWT = this.obterTokenJWT(request);
        ResponseEntity<String> result = this.restTemplate.getForEntity("http://localhost:8082/usuarios/role/" + tokenJWT, String.class);
        String roleUsuario = result.getBody();
        if(!role.corresponde(roleUsuario)) {
            throw new AnotacaoException("Não autorizado", "Você não tem permissão para acessar esse endpoint", "", "403");
        }
    }

    private String obterTokenJWT(HttpServletRequest request) {
        String tokenJWT = request.getHeader("AUTHORIZATION");
        this.verificarSeCampoHeaderAuthorizationExiste(tokenJWT);
        this.verificarSeValorCampoAuthorizationTemPrefixoHeader(tokenJWT);
        return tokenJWT.replace("Bearer ", "");
    }

    private void verificarSeCampoHeaderAuthorizationExiste(String tokenJwt) {
        if(tokenJwt == null) {
            throw new AnotacaoException("Falha na autenticação", "Cabeçalho não contém campo 'AUTHORIZATION' contendo um token JWT", "", "403");
        }
    }

    private void verificarSeValorCampoAuthorizationTemPrefixoHeader(String tokenJwt) {
        if(!tokenJwt.startsWith("Bearer ")) {
            throw new AnotacaoException("Falha na autenticação", "Campo do Header 'AUTHORIZATION' não inicia com o prefixo 'Bearer '", "", "403");
        }
    }

}
