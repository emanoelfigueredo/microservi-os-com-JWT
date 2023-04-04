package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.domain.UsuarioRepository;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.exception.IdentidadeException;
import com.efigueredo.service_identidade.service.exception.UsernameExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void salvarUsuario(DtoRegistroRequisicao dto) throws IdentidadeException {
        this.verificarSeUsernameJaFoiUtilizado(dto.getUsername());
        Usuario usuario = dto.toUsuario();
        usuario.setSenha(this.encoder.encode(dto.getSenha()));
        this.usuarioRepository.save(usuario);
    }

    private void verificarSeUsernameJaFoiUtilizado(String username) throws IdentidadeException {
        Boolean usernameJaUtilizado = this.usuarioRepository.existsByUsername(username);
        if(usernameJaUtilizado) {
            throw new UsernameExistenteException("O username " + username + " nao esta disponivel");
        }
    }

}
