package com.efigueredo.service_identidate.infra.controller;

import com.efigueredo.service_identidate.service.RegistroUsuarioService;
import com.efigueredo.service_identidate.service.TokenJwt;
import com.efigueredo.service_identidate.service.TokenJwtService;
import com.efigueredo.service_identidate.service.dto.DtoAutenticacao;
import com.efigueredo.service_identidate.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidate.service.exception.IdentidadeException;
import com.efigueredo.service_identidate.service.exception.NaoAutenticadoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("identidade")
public class IdentidadeController {

    @Autowired
    private RegistroUsuarioService autenticacaoService;
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenJwtService tokenJwtService;

    @PostMapping("registrar")
    public ResponseEntity registrar(@RequestBody @Valid DtoRegistroRequisicao dto) throws IdentidadeException {
        this.autenticacaoService.salvarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("autenticar")
    public ResponseEntity<TokenJwt> autenticacao(@RequestBody @Valid DtoAutenticacao dto) throws IdentidadeException {
        this.autenticar(dto);
        TokenJwt tokenJwt = this.tokenJwtService.gerarToken(dto.getUsername());
        return ResponseEntity.ok().body(tokenJwt);
    }

    private void autenticar(DtoAutenticacao dto) throws IdentidadeException {
        Authentication autenticacao = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getSenha()));
        if(!autenticacao.isAuthenticated()) {
            throw new NaoAutenticadoException("Credenciais invalidas");
        }
    }

}
