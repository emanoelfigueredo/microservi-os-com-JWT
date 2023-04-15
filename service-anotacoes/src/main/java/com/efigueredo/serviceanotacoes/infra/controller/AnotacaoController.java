package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.infra.handler.exceptions.DtoErro;
import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import com.efigueredo.serviceanotacoes.service.roles.Roles;
import com.efigueredo.serviceanotacoes.service.roles.RolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "ADMIN - Obtenha todas as anotações do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição processada com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @GetMapping
    public ResponseEntity<Page<DtoAnotacoesResposta>> listarAnotacoes(@PageableDefault(size = 10) Pageable paginacao, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        Page<DtoAnotacoesResposta> dados = this.anotacaoService.obterTodas(paginacao);
        return ResponseEntity.ok().body(dados);
    }

    @Operation(summary = "ADMIN | USUARIO - Obtenha uma anotações por seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoAnotacoesResposta.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @GetMapping("{id}")
    public ResponseEntity<DtoAnotacoesResposta> buscarAnotacao(@PathVariable Long id, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.USUARIO);
        DtoAnotacoesResposta anotacaoDto = this.anotacaoService.obterAnotacao(id);
        return ResponseEntity.ok().body(anotacaoDto);
    }

    @Operation(summary = "ADMIN | USUARIO - Adicione uma anotação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Anotação criada no sistema", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoAnotacoesResposta.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> adicionarAnotacao(@RequestBody @Valid DtoAnotacoesCadastroRequisicao dto,
                                                                  UriComponentsBuilder uriBuilder, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        var dtoResposta = this.anotacaoService.criarAnotacao(dto);
        var uri = uriBuilder.path("/anotacao/{id}").buildAndExpand(dtoResposta.getId()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    @Operation(summary = "ADMIN - Altere uma anotaçõo por seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requisição concluída", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoAnotacoesResposta.class))}),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "404", description = "Anotação de id informado não existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> atuazaliarAnotacao(@PathVariable Long id, @RequestBody @Valid
                                                                DtoAnotacoesCadastroRequisicao dto, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        DtoAnotacoesResposta dtoAnotacoesResposta = this.anotacaoService.atualizarAnotacao(id, dto);
        return ResponseEntity.ok().body(dtoAnotacoesResposta);
    }

    @Operation(summary = "ADMIN - Remova uma anotaçõo por seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Requisição concluída"),
            @ApiResponse(responseCode = "403", description = "Proibido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))}),
            @ApiResponse(responseCode = "404", description = "Anotação de id informado não existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DtoErro.class))})
    })
    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> removerAnotacao(@PathVariable Long id, HttpServletRequest request) {
        this.rolesService.verificarAutorizacaoDoUsuario(request, Roles.ADMINISTRADOR);
        this.anotacaoService.removerAnotacao(id);
        return ResponseEntity.noContent().build();
    }

}
