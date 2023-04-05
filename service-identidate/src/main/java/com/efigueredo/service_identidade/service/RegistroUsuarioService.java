package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.RolesUsuario;
import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.domain.UsuarioRepository;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.exception.IdentidadeException;
import com.efigueredo.service_identidade.service.exception.UsernameExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroUsuarioService extends ServiceIdentity {

    @Autowired
    private PasswordEncoder encoder;

    public void salvarUsuario(DtoRegistroRequisicao dto) throws IdentityException {
        this.verificarSeUsernameJaFoiUtilizado(dto.getUsername());
        Usuario usuario = dto.toUsuario();
        usuario.setRoles(RolesUsuario.ROLE_USUARIO.toString());
        usuario.setActive(true);
        usuario.setSenha(this.encoder.encode(dto.getSenha()));
        super.usuarioRepository.save(usuario);
    }

}
