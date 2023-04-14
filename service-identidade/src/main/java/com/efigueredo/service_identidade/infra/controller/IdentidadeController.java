package com.efigueredo.service_identidade.infra.controller;

import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.service.TokenJwt;
import com.efigueredo.service_identidade.service.TokenJwtService;
import com.efigueredo.service_identidade.service.UsuarioService;
import com.efigueredo.service_identidade.service.dto.DtoAutenticacao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("identidade")
public class IdentidadeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenJwtService tokenJwtService;

    @PostMapping("registrar")
    public ResponseEntity registrar(@RequestBody @Valid DtoRegistroRequisicao dto) throws IdentityException {
        this.usuarioService.salvarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("autenticar")
    public ResponseEntity<TokenJwt> autenticacao(@RequestBody @Valid DtoAutenticacao dto) throws IdentityException {
        this.usuarioService.autenticar(dto);
        TokenJwt tokenJwt = this.tokenJwtService.gerarToken(dto.username());
        return ResponseEntity.ok().body(tokenJwt);
    }

    @GetMapping("validar")
    public ResponseEntity<String> validar(@RequestParam("token") String token) throws IdentityException {
        this.tokenJwtService.validarToken(token);
        return ResponseEntity.ok().build();
    }

}
