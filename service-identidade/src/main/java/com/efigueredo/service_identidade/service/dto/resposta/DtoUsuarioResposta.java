package com.efigueredo.service_identidade.service.dto.resposta;

import com.efigueredo.service_identidade.domain.Usuario;

public record DtoUsuarioResposta(String nome, String username, Boolean active) {

    public DtoUsuarioResposta(Usuario usuario) {
        this(usuario.getNome(), usuario.getUsername(), usuario.getActive());
    }

}
