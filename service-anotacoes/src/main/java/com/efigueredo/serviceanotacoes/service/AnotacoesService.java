package com.efigueredo.serviceanotacoes.service;

import com.efigueredo.serviceanotacoes.domain.Anotacao;
import com.efigueredo.serviceanotacoes.domain.AnotacaoRepository;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnotacoesService {

    @Autowired
    private AnotacaoRepository anotacaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<DtoAnotacoesResposta> obterTodas(Pageable paginacao) {
        return this.anotacaoRepository.findAll(paginacao)
                .map(entidade -> this.modelMapper.map(entidade, DtoAnotacoesResposta.class));
    }

    public DtoAnotacoesResposta obterAnotacao(Long id) {
        this.verificarSeAnotacaoDeIdExiste(id);
        Anotacao anotacao = this.anotacaoRepository.findById(id).get();
        return this.modelMapper.map(anotacao, DtoAnotacoesResposta.class);
    }

    public DtoAnotacoesResposta criarAnotacao(DtoAnotacoesCadastroRequisicao dtoCadastro) {
        Anotacao anotacao = this.modelMapper.map(dtoCadastro, Anotacao.class);
        anotacao.setMomento(LocalDateTime.now());
        this.anotacaoRepository.save(anotacao);
        return this.modelMapper.map(anotacao, DtoAnotacoesResposta.class);
    }

    public void removerAnotacao(Long id) {
        this.verificarSeAnotacaoDeIdExiste(id);
        this.anotacaoRepository.deleteById(id);
    }

    public DtoAnotacoesResposta atualizarAnotacao(Long id, DtoAnotacoesCadastroRequisicao dto) {
        this.verificarSeAnotacaoDeIdExiste(id);
        Anotacao anotacao = this.anotacaoRepository.findById(id).get();
        anotacao.setTitulo(dto.getTitulo());
        anotacao.setConteudo(dto.getConteudo());
        this.anotacaoRepository.save(anotacao);
        return this.modelMapper.map(anotacao, DtoAnotacoesResposta.class);
    }

    private void verificarSeAnotacaoDeIdExiste(Long id) {
        if(!this.anotacaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Anotação de id " + id + " não existe.");
        }
    }
}
