package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.domain.UsuarioRepository;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.dto.resposta.DtoUsuarioResposta;
import com.efigueredo.service_identidade.service.exception.IdentidadeException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService extends ServiceIdentity {

    @Autowired
    private PasswordEncoder encoder;

    public Page<DtoUsuarioResposta> listarUsuarios(Pageable paginacao) {
        return super.usuarioRepository.findAll(paginacao).map(DtoUsuarioResposta::new);
    }

    public DtoUsuarioResposta obterUsuarioPorId(Long id) throws IdentityException {
        Optional<Usuario> optionalUsuario = super.usuarioRepository.findById(id);
        if(optionalUsuario.isEmpty()) {
            throw new IdentityException("Valor invalido", "Usuario de id " + id + " nao existe no sistema", "", "404");
        }
        return new DtoUsuarioResposta(optionalUsuario.get());
    }

    public DtoUsuarioResposta obterUsuarioPorUsername(String username) throws IdentityException {
        Optional<Usuario> optionalUsuario = super.usuarioRepository.findByUsername(username);
        if(optionalUsuario.isEmpty()) {
            throw new IdentityException("Valor invalido", "Usuario de username '" + username + "' nao existe no sistema", "", "404");
        }
        return new DtoUsuarioResposta(optionalUsuario.get());
    }

    public CustomUserDetails obterProprioUsuario(String senha) throws IdentityException {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder
                                                        .getContext()
                                                        .getAuthentication()
                                                        .getPrincipal();
        if(!this.encoder.matches(senha, user.getPassword())) {
            throw new IdentityException("Credencial inválida", "Senha inserida é inválida", "", "422");
        }
        user.setSenha(senha);
        return user;
    }

    public void removerUsuario(Long id) throws IdentityException {
        Optional<Usuario> optionalUsuario = super.usuarioRepository.findById(id);
        if(optionalUsuario.isEmpty()) {
            throw new IdentityException("Valor invalido", "Usuario de id " + id + " nao existe no sistema", "", "404");
        }
        Usuario usuario = optionalUsuario.get();
        usuario.setActive(false);
        super.usuarioRepository.save(usuario);
    }

    public Usuario alterarUsuario(Long id, DtoRegistroRequisicao dto) throws IdentityException {
        Optional<Usuario> optionalUsuario = super.usuarioRepository.findById(id);
        if(optionalUsuario.isEmpty()) {
            throw new IdentityException("Valor invalido", "Usuario de id " + id + " nao existe no sistema", "", "404");
        }
        Usuario usuario = optionalUsuario.get();
        usuario.setNome(dto.getNome());
        usuario.setSenha(this.encoder.encode(dto.getSenha()));
        boolean usernameNaoFoiAlterado = usuario.getUsername().equals(dto.getUsername()) ;
        if(!usernameNaoFoiAlterado) {
            super.verificarSeUsernameJaFoiUtilizado(dto.getUsername());
            usuario.setUsername(dto.getUsername());
        }
        return super.usuarioRepository.save(usuario);
    }

    public void desativarUsuarioProprio() throws IdentityException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = super.usuarioRepository.findByUsername(userDetails.getUsername()).get();
        boolean ehAdministador = usuario.getRoles().contains("ROLE_ADMINISTRADOR");
        if(ehAdministador) {
            throw new IdentityException("Operação inválida", "Administrador não pode se desativar", "", "400");
        }
        usuario.setActive(false);
        super.usuarioRepository.save(usuario);
    }


    public void ativarUsuarioProprio() throws IdentityException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = super.usuarioRepository.findByUsername(userDetails.getUsername()).get();
        boolean ehAdministador = usuario.getRoles().contains("ROLE_ADMINISTRADOR");
        if(ehAdministador) {
            throw new IdentityException("Operação inválida", "Administrador não pode se desativar", "", "400");
        }
        usuario.setActive(true);
        super.usuarioRepository.save(usuario);
    }

    public Usuario alterarPropriasCredenciais(DtoRegistroRequisicao dto) throws IdentityException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = super.usuarioRepository.findByUsername(userDetails.getUsername()).get();
        usuario.setNome(dto.getNome());
        usuario.setSenha(this.encoder.encode(dto.getSenha()));
        boolean usernameNaoFoiAlterado = usuario.getUsername().equals(dto.getUsername());
        if(!usernameNaoFoiAlterado) {
            super.verificarSeUsernameJaFoiUtilizado(dto.getUsername());
            usuario.setUsername(dto.getUsername());
        }
        return super.usuarioRepository.save(usuario);
    }
}
