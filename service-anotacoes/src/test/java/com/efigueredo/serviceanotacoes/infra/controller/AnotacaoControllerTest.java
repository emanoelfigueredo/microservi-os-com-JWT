package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.domain.Anotacao;
import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
public class AnotacaoControllerTest {

    @Autowired
    private AnotacoesService anotacaoService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JacksonTester<Page<DtoAnotacoesResposta>> dtoRespostaAnotacoes;

//    @Test
//    @DisplayName("Deveria retornar código 200 e no corpo a lista de anotações")
//    public void listar_cenario1() throws Exception {
//        Page<Anotacao> pageAnotacoes = this.obterPageAnotacao(this.obterListaAnotacoes());
//        Page<DtoAnotacoesResposta> pageDtoAnotacoesResposta = this.obterPageDtoRespostaAnotacao(pageAnotacoes);
//        when(this.anotacaoService.obterTodas(any(PageRequest.class))).thenReturn(pageDtoAnotacoesResposta);
//        MockHttpServletResponse response = this.mvc.perform(
//                                                        get("/anotacoes")
//                                                    ).andReturn().getResponse();
//        System.out.println(response.getContentAsString());
//        String jsonEsperado = this.dtoRespostaAnotacoes.write(pageDtoAnotacoesResposta).getJson();
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
//    }

    private List<Anotacao> obterListaAnotacoes() {
        return List.of(
                new Anotacao(1l, "Titulo1", "Conteudo 1", LocalDateTime.now()),
                new Anotacao(2l, "Titulo2", "Conteudo 2", LocalDateTime.now()),
                new Anotacao(3l, "Titulo3", "Conteudo 3", LocalDateTime.now())
        );
    }

    private Page<Anotacao> obterPageAnotacao(List<Anotacao> anotacoes) {
        return new PageImpl<Anotacao>(anotacoes);
    }

    private Page<DtoAnotacoesResposta> obterPageDtoRespostaAnotacao(Page<Anotacao> pageAnotacoes) {
        return pageAnotacoes.map(anotacao -> this.modelMapper.map(anotacao, DtoAnotacoesResposta.class));
    }

}
