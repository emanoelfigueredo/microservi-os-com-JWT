<<<<<<< HEAD
package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.domain.Anotacao;
import com.efigueredo.serviceanotacoes.infra.handler.exceptions.DtoErro;
import com.efigueredo.serviceanotacoes.service.AnotacoesService;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
public class AnotacaoControllerTest {

    @MockBean
    private AnotacoesService anotacaoService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JacksonTester<Page<DtoAnotacoesResposta>> dtoPageRespostaAnotacoes;

    @Autowired
    private JacksonTester<DtoErro> dtoErro;

    @Autowired
    private JacksonTester<DtoAnotacoesResposta> dtoRespostaAnotacoes;


    @Autowired
    private JacksonTester<DtoAnotacoesCadastroRequisicao> dtoCadastroAnotacao;

//    @Test
//    @DisplayName("Deveria retornar código 200 e no conteudo a lista de anotações")
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

    @Test
    @DisplayName("Deveria retornar código 201 e o JSON da anotação criada")
    public void criar_anotacao_cenario1() throws Exception {
        var dtoCadastro = new DtoAnotacoesCadastroRequisicao("titulo", "conteudo");
        var dtoResposta = new DtoAnotacoesResposta(null, "titulo", "conteudo", LocalDateTime.now());
        when(this.anotacaoService.criarAnotacao(dtoCadastro)).thenReturn(dtoResposta);
        var response = this.mvc.perform(
                post("/anotacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoCadastroAnotacao.write(dtoCadastro).getJson())
        ).andReturn().getResponse();
        var jsonEsperado = this.dtoRespostaAnotacoes.write(dtoResposta).getJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria retornar código 422 quando o JSON do POST estiver com campo titulo incorreto")
    public void criar_anotacao_cenario3() throws Exception {

        JSONObject jsonEnviadoCampoTituloIncorreto = new JSONObject();
        jsonEnviadoCampoTituloIncorreto.put("tituto-errado", "valor");
        jsonEnviadoCampoTituloIncorreto.put("conteudo", "valor");

        var response = this.mvc.perform(
                post("/anotacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEnviadoCampoTituloIncorreto.toString())
        ).andReturn().getResponse();

        DtoErro dtoErroResponse = new DtoErro("Campo invalido", "Campo 'titulo' incorreto", "", "422");
        String jsonReponseEsperado = this.dtoErro.write(dtoErroResponse).getJson();
        List<String> jsons = List.of(jsonReponseEsperado);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo(jsons.toString());
    }

    @Test
    @DisplayName("Deveria retornar código 422 quando o JSON do POST estiver com campo conteudo incorreto")
    public void criar_anotacao_cenario4() throws Exception {

        JSONObject jsonEnviadoCampoTituloIncorreto = new JSONObject();
        jsonEnviadoCampoTituloIncorreto.put("titulo", "valor");
        jsonEnviadoCampoTituloIncorreto.put("conteudo-errado", "valor");

        var response = this.mvc.perform(
                post("/anotacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEnviadoCampoTituloIncorreto.toString())
        ).andReturn().getResponse();

        DtoErro dtoErroResponse = new DtoErro("Campo invalido", "Campo 'conteudo' incorreto", "", "422");
        String jsonReponseEsperado = this.dtoErro.write(dtoErroResponse).getJson();
        List<String> jsons = List.of(jsonReponseEsperado);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo(jsons.toString());
    }

    @Test
    @DisplayName("Deveria retornar código 422 quando o JSON do POST estiver com campos incorretos")
    public void criar_anotacao_cenario2() throws Exception {

        JSONObject jsonEnviadoCampoTituloIncorreto = new JSONObject();
        jsonEnviadoCampoTituloIncorreto.put("tituto-errado", "valor");
        jsonEnviadoCampoTituloIncorreto.put("conteudo-errado", "valor");

        var response = this.mvc.perform(
                post("/anotacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEnviadoCampoTituloIncorreto.toString())
        ).andReturn().getResponse();

        DtoErro dtoErroResponseTitulo = new DtoErro("Campo invalido", "Campo 'titulo' incorreto", "", "422");
        DtoErro dtoErroResponseconteudo = new DtoErro("Campo invalido", "Campo 'conteudo' incorreto", "", "422");
        String jsonReponseEsperadoTitulo = this.dtoErro.write(dtoErroResponseTitulo).getJson();
        String jsonReponseEsperadoconteudo = this.dtoErro.write(dtoErroResponseconteudo).getJson();
        List<String> jsons = List.of(jsonReponseEsperadoTitulo + "," + jsonReponseEsperadoconteudo);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(response.getContentAsString()).isEqualTo(jsons.toString());
    }

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
=======
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
>>>>>>> 53d1cfec92158846490aa39cdc9c0d52f97104f5
