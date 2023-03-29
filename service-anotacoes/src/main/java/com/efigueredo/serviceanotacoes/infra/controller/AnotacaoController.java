package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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

    @GetMapping
    public ResponseEntity<Page<DtoAnotacoesResposta>> listarAnotacoes(@PageableDefault(size = 10) Pageable paginacao) {
        System.out.println(paginacao);
        System.out.println(paginacao.getClass());
        Page<DtoAnotacoesResposta> dados = this.anotacaoService.obterTodas(paginacao);
        return ResponseEntity.ok().body(dados);
    }

    @GetMapping("{id}")
    public ResponseEntity<DtoAnotacoesResposta> buscarAnotacao(@PathVariable Long id) {
        DtoAnotacoesResposta anotacaoDto = this.anotacaoService.obterAnotacao(id);
        return ResponseEntity.ok().body(anotacaoDto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> adicionarAnotacao(@RequestBody @Valid DtoAnotacoesCadastroRequisicao dto, UriComponentsBuilder uriBuilder) {
        var dtoResposta = this.anotacaoService.criarAnotacao(dto);
        var uri = uriBuilder.path("/anotacao/{id}").buildAndExpand(dtoResposta.getId()).toUri();
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<DtoAnotacoesResposta> atuazaliarAnotacao(@PathVariable Long id, @RequestBody @Valid DtoAnotacoesCadastroRequisicao dto) {
        DtoAnotacoesResposta dtoAnotacoesResposta = this.anotacaoService.atualizarAnotacao(id, dto);
        return ResponseEntity.ok().body(dtoAnotacoesResposta);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> removerAnotacao(@PathVariable Long id) {
        this.anotacaoService.removerAnotacao(id);
        return ResponseEntity.noContent().build();
    }

}
