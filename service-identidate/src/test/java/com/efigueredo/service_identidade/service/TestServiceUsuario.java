package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class TestServiceUsuario {

    @Autowired
    protected TestEntityManager em;

    @Autowired
    protected PasswordEncoder encoder;

    protected Usuario cadastrarUsuario(String nome, String username, String senha, String roles, boolean ativo, boolean senhaCriptografada) {
        if(senhaCriptografada) {
            senha = this.encoder.encode(senha);
        }
        Usuario usuario = new Usuario(null, nome, username, senha, roles, ativo);
        return this.em.persist(usuario);
    }

    protected Usuario setarUsuarioLogado(String nome, String username, String senha, String roles, boolean ativo, boolean senhaCriptografada) {
        Usuario usuario = this.cadastrarUsuario(nome, username, senha, roles, ativo, senhaCriptografada);
        CustomUserDetails userDetails = new CustomUserDetails(usuario);
        Authentication autenticacao = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
        return usuario;
    }
}
