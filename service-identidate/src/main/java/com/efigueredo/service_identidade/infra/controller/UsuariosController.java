package com.efigueredo.service_identidade.infra.controller;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.UsuarioService;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoSenha;
import com.efigueredo.service_identidade.service.dto.resposta.DtoUsuarioResposta;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;


    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<DtoUsuarioResposta>> listarUsuarios(@PageableDefault(size=15) Pageable paginacao) {
        Page<DtoUsuarioResposta> pageUsuarios = this.usuarioService.listarUsuarios(paginacao);
        return ResponseEntity.ok(pageUsuarios);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping("id/{id}")
    public ResponseEntity<DtoUsuarioResposta> obterUsuarioPorId(@PathVariable Long id) throws IdentityException {
        DtoUsuarioResposta usuarioDto = this.usuarioService.obterUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping("username/{username}")
    public ResponseEntity<DtoUsuarioResposta> obterUsuarioPorUsername(@PathVariable String username) throws IdentityException {
        DtoUsuarioResposta usuarioDto = this.usuarioService.obterUsuarioPorUsername(username);
        return ResponseEntity.ok(usuarioDto);
    }

    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PostMapping("my")
    public ResponseEntity<CustomUserDetails> obterSuasCredenciais(@RequestBody DtoSenha dto) throws IdentityException {
        CustomUserDetails usuario = this.usuarioService.obterProprioUsuario(dto.senha());
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @DeleteMapping("{id}")
    public ResponseEntity deletarUsuario(@PathVariable Long id) throws IdentityException {
        this.usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @PutMapping("{id}")
    public ResponseEntity<Usuario> alterarCredenciais(@RequestBody @Valid DtoRegistroRequisicao dto, @PathVariable Long id) throws IdentityException {
        Usuario usuario = this.usuarioService.alterarUsuario(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping
    public ResponseEntity<Usuario> alterarCredenciaisProprias(@RequestBody @Valid DtoRegistroRequisicao dto) throws IdentityException {
        Usuario usuario = this.usuarioService.alterarPropriasCredenciais(dto);
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping("desativar")
    public ResponseEntity desativarProprioUsuario() throws IdentityException {
        this.usuarioService.desativarUsuarioProprio();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping("ativar")
    public ResponseEntity ativarProprioUsuario() throws IdentityException {
        this.usuarioService.ativarUsuarioProprio();
        return ResponseEntity.ok().build();
    }

    @PostMapping("role/{token}")
    public ResponseEntity<String> obterRoles(@PathVariable String token) throws IdentityException {
        String role = this.usuarioService.obterRolePorTokenJwt(token);
        return ResponseEntity.ok(role);
    }

}
