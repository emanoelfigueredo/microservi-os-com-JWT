package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.UsuarioRepository;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceIdentity {

    @Autowired
    protected UsuarioRepository usuarioRepository;

    protected void verificarSeUsernameJaFoiUtilizado(String username) throws IdentityException {
        Boolean usernameJaUtilizado = this.usuarioRepository.existsByUsername(username);
        System.out.println(usernameJaUtilizado);
        if(usernameJaUtilizado) {
            throw new IdentityException("Credencial invalida", "O username " + username + " nao esta disponivel", "", "400");
        }
    }

}
