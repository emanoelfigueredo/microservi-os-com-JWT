package com.efigueredo.service_identidade.infra.controller;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.infra.conf.exception.DtoErro;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.TokenJwt;
import com.efigueredo.service_identidade.service.TokenJwtService;
import com.efigueredo.service_identidade.service.UsuarioService;
import com.efigueredo.service_identidade.service.dto.DtoAutenticacao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoSenha;
import com.efigueredo.service_identidade.service.dto.resposta.DtoUsuarioResposta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
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

    @Autowired
    private TokenJwtService tokenJwtService;

    @Operation(summary = "Registre-se no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado no sistema."),
            @ApiResponse(responseCode = "422", description = "JSON enviado inválido.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))
            })
    })
    @PostMapping("registrar")
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DtoRegistroRequisicao dto) throws IdentityException {
        this.usuarioService.salvarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Autentique-se no sistema e receba um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciais válidas. Autenticação concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenJwt.class))}),
            @ApiResponse(responseCode = "422", description = "JSON enviado inválido.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PostMapping("autenticar")
    public ResponseEntity<TokenJwt> autenticacao(@RequestBody @Valid DtoAutenticacao dto) throws IdentityException {
        this.usuarioService.autenticar(dto);
        TokenJwt tokenJwt = this.tokenJwtService.gerarToken(dto.username());
        System.out.println(tokenJwt);
        return ResponseEntity.ok().body(tokenJwt);
    }

    @Operation(summary = "Valide seu token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verifique se o token é válido e receba as roles do usuário como resposta.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @GetMapping("token/validar")
    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    public ResponseEntity<String> validar(@RequestParam("token") String token) throws IdentityException {
        this.tokenJwtService.validarToken(token);
        String roles = this.usuarioService.obterRolePorTokenJwt(token);
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "ADMIN - Obtenha todos os usuários do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Page<DtoUsuarioResposta>> listarUsuarios(@PageableDefault(size=15) Pageable paginacao) {
        Page<DtoUsuarioResposta> pageUsuarios = this.usuarioService.listarUsuarios(paginacao);
        return ResponseEntity.ok(pageUsuarios);
    }

    @Operation(summary = "ADMIN - Obtenha um usuário pelo seu id único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado. Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoUsuarioResposta.class))}),
            @ApiResponse(responseCode = "404", description = "Usuário de id fornecido não existe no sistema", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping("id/{id}")
    public ResponseEntity<DtoUsuarioResposta> obterUsuarioPorId(@PathVariable Long id) throws IdentityException {
        DtoUsuarioResposta usuarioDto = this.usuarioService.obterUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDto);
    }

    @Operation(summary = "ADMIN - Obtenha um usuário pelo seu username único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado. Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoUsuarioResposta.class))}),
            @ApiResponse(responseCode = "404", description = "Usuário de username fornecido não existe no sistema", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @GetMapping("username/{username}")
    public ResponseEntity<DtoUsuarioResposta> obterUsuarioPorUsername(@PathVariable String username) throws IdentityException {
        DtoUsuarioResposta usuarioDto = this.usuarioService.obterUsuarioPorUsername(username);
        return ResponseEntity.ok(usuarioDto);
    }

    @Operation(summary = "USUARIO | ADMIN - Obtenha todas as informações de seu usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado. Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomUserDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Senha fornecida é inválida", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "422", description = "JSON enviado é inválido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PostMapping("my")
    public ResponseEntity<CustomUserDetails> obterSuasCredenciais(@RequestBody DtoSenha dto) throws IdentityException {
        CustomUserDetails usuario = this.usuarioService.obterProprioUsuario(dto.senha());
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "ADMIN - Remova logicamente um usuário. Ou desative-o")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário desativado do sistema."),
            @ApiResponse(responseCode = "404", description = "Usuário de id fornecido não existe no sistema.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity deletarUsuario(@PathVariable Long id) throws IdentityException {
        this.usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "ADMIN - Altere as credenciais de um usuário pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações do usuário alteradas com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404", description = "Usuáiro de id fornecido não existe no sistema.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "422", description = "JSON enviado é inválido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<Usuario> alterarCredenciais(@RequestBody @Valid DtoRegistroRequisicao dto, @PathVariable Long id) throws IdentityException {
        Usuario usuario = this.usuarioService.alterarUsuario(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "USUARIO | ADMIN - Altere suas próprias credenciais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações do próprio usuário alteradas com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "422", description = "JSON enviado é inválido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping
    @Transactional
    public ResponseEntity<Usuario> alterarCredenciaisProprias(@RequestBody @Valid DtoRegistroRequisicao dto) throws IdentityException {
        Usuario usuario = this.usuarioService.alterarPropriasCredenciais(dto);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "USUARIO | ADMIN - Desative seu proprio usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta desativada com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomUserDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Administrador não pode desativar a conta.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping("desativar")
    @Transactional
    public ResponseEntity desativarProprioUsuario() throws IdentityException {
        this.usuarioService.desativarUsuarioProprio();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "USUARIO | ADMIN - Ative seu proprio usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta ativada com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomUserDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Administrador não pode ativar a conta.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PreAuthorize("hasAuthority('ROLE_USUARIO')")
    @PutMapping("ativar")
    @Transactional
    public ResponseEntity ativarProprioUsuario() throws IdentityException {
        this.usuarioService.ativarUsuarioProprio();
        return ResponseEntity.ok().build();
    }

}
