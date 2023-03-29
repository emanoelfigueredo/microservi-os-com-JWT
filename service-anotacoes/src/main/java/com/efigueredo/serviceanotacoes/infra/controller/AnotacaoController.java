package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("id")
    public ResponseEntity<String> buscarAnotacao() {
        return null;
    }

    @PostMapping
    public ResponseEntity<String> adicionarAnotacao() {
        return null;
    }

    @PutMapping
    public ResponseEntity<String> atuazaliarAnotacao() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<String> removerAnotacao() {
        return null;
    }

}
