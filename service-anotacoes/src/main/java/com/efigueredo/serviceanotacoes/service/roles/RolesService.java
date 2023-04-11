package com.efigueredo.serviceanotacoes.service.roles;

import com.efigueredo.serviceanotacoes.infra.client.UsuarioRoleClient;
import com.efigueredo.serviceanotacoes.infra.handler.exceptions.AnotacaoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {

    @Autowired
    private UsuarioRoleClient usuarioRoleClient;

    public void verificarAutorizacaoDoUsuario(HttpServletRequest request, Roles role) {
        String tokenJWT = this.obterTokenJWT(request);
        String roleUsuario = this.usuarioRoleClient.obterRolePorTokenJwt(tokenJWT);
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
