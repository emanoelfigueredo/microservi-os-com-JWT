package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import com.efigueredo.serviceanotacoes.service.roles.Roles;
import com.efigueredo.serviceanotacoes.service.roles.RolesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("anotacoes")
public class AnotacaoController {

    @Autowired
    private AnotacoesService anotacaoService;

    @Autowired
    private RolesService rolesService;

    @GetMapping
    public ResponseEntity<Page<DtoAnotacoesResposta>> listarAnotacoes(@PageableDefault(size = 10) Pageable paginacao, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        Page<DtoAnotacoesResposta> dados = this.anotacaoService.obterTodas(paginacao);
        return ResponseEntity.ok().body(dados);
    }

    @GetMapping("{id}")
    public ResponseEntity<DtoAnotacoesResposta> buscarAnotacao(@PathVariable Long id, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.USUARIO);
        DtoAnotacoesResposta anotacaoDto = this.anotacaoService.obterAnotacao(id);
        return ResponseEntity.ok().body(anotacaoDto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> adicionarAnotacao(@RequestBody @Valid DtoAnotacoesCadastroRequisicao dto,
                                                                  UriComponentsBuilder uriBuilder, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        var dtoResposta = this.anotacaoService.criarAnotacao(dto);
        var uri = uriBuilder.path("/anotacao/{id}").buildAndExpand(dtoResposta.getId()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> atuazaliarAnotacao(@PathVariable Long id, @RequestBody @Valid
                                                                DtoAnotacoesCadastroRequisicao dto, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        DtoAnotacoesResposta dtoAnotacoesResposta = this.anotacaoService.atualizarAnotacao(id, dto);
        return ResponseEntity.ok().body(dtoAnotacoesResposta);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> removerAnotacao(@PathVariable Long id, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        this.anotacaoService.removerAnotacao(id);
        return ResponseEntity.noContent().build();
    }

}
