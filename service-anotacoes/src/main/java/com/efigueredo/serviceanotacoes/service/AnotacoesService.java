package com.efigueredo.serviceanotacoes.service;

import com.efigueredo.serviceanotacoes.domain.AnotacaoRepository;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnotacoesService {

    @Autowired
    private AnotacaoRepository anotacaoRepository;

    @Autowired
    private ModelMapper modelMapper;

//    public void adicionarAnotacao(Anotacao anotacao) {
//        this.anotacaoRepository.save(anotacao);
//    }

    public Page<DtoAnotacoesResposta> obterTodas(Pageable paginacao) {
        return this.anotacaoRepository.findAll(paginacao)
                .map(entidade -> this.modelMapper.map(entidade, DtoAnotacoesResposta.class));


    }

}
