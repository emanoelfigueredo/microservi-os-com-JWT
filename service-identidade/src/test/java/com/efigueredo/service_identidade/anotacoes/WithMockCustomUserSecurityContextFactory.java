package com.efigueredo.service_identidade.anotacoes;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser dadosUsuario) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Usuario usuario = new Usuario(null, dadosUsuario.nome(), dadosUsuario.username(), dadosUsuario.senha(), dadosUsuario.roles(), true);
        CustomUserDetails principal = new CustomUserDetails(usuario);
        Authentication autenticacao = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(autenticacao);
        return context;
    }

}
