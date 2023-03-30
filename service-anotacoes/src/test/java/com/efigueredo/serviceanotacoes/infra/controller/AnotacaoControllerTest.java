package com.efigueredo.serviceanotacoes.infra.controller;

import com.efigueredo.serviceanotacoes.infra.handler.exceptions.DtoErro;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Test
    @DisplayName("Deveria retornar código 201 e o JSON da anotação criada")
    public void criar_anotacao_cenario1() throws Exception {
        var dtoCadastro = new DtoAnotacoesCadastroRequisicao(null, "titulo", "conteudo");
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
        DtoErro dtoErroResponseConteudo = new DtoErro("Campo invalido", "Campo 'conteudo' incorreto", "", "422");
        String jsonReponseEsperadoTitulo = this.dtoErro.write(dtoErroResponseTitulo).getJson();
        String jsonReponseEsperadoConteudo = this.dtoErro.write(dtoErroResponseConteudo).getJson();

        // Devido a possibilidade do objeto json de titulo e conteudo vierem na reposta em posições diferentes,
        // é necessário verificar qual vem primeiro para realizar a assertiva.
        String body = response.getContentAsString();
        int posicaoDoCampoTitulo = body.indexOf("'titulo'");
        int posicaoDoCampoConteudo = body.indexOf("'conteudo'");
        if(posicaoDoCampoTitulo < posicaoDoCampoConteudo) {
            List<String> jsons = List.of(jsonReponseEsperadoTitulo + "," + jsonReponseEsperadoConteudo);
            assertThat(body).isEqualTo(jsons.toString());
        } else {
            List<String> jsons = List.of(jsonReponseEsperadoConteudo + "," + jsonReponseEsperadoTitulo);
            assertThat(body).isEqualTo(jsons.toString());
        }
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    @DisplayName("Deveria retornar código 204 quando a remoção de anotação for concluída")
    public void remover_anotacao_cenario1() throws Exception {
        doNothing().when(this.anotacaoService).removerAnotacao(1l);
        var response = this.mvc.perform(
                                delete("/anotacoes/1")
                        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Deveria retornar código 404 quando for solicitado a remoção de uma anotação de ID inexistente")
    public void remover_anotacao_cenario2() throws Exception {

        var ex = new EntityNotFoundException("Anotacao de id 20 nao existe");
        doThrow(ex).when(this.anotacaoService).removerAnotacao(20l);
        var response = this.mvc.perform(
                delete("/anotacoes/20")
        ).andReturn().getResponse();

        DtoErro dtoErroResponse = new DtoErro("Anotacao inexistente", "Anotacao de id 20 nao existe", "", "404");
        String jsonReponseEsperado = this.dtoErro.write(dtoErroResponse).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonReponseEsperado);
    }

    @Test
    @DisplayName("Deveria retonrar código 200 quando requisição de atualização de anotação for bem sucedido")
    public void atualizar_anotacao_cenario1() throws Exception {
        var dtoRequisicao = new DtoAnotacoesCadastroRequisicao(
                null, "titulo alterado", "conteudo alterado"
        );
        var dtoRetornoService = new DtoAnotacoesResposta(
                1l, "titulo alterado", "conteudo alterado", LocalDateTime.now()
        );

        when(this.anotacaoService.atualizarAnotacao(1l, dtoRequisicao))
                .thenReturn(dtoRetornoService);

        var jsonRequisicaoAlteracao = this.dtoCadastroAnotacao.write(dtoRequisicao).getJson();
        var response = this.mvc.perform(
                put("/anotacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequisicaoAlteracao)
        ).andReturn().getResponse();
        var jsonEsperadoResposta = this.dtoRespostaAnotacoes.write(dtoRetornoService).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperadoResposta);
    }

    @Test
    @DisplayName("Deveria retonrar código 404 quando requisição de atualização vir com id inexistente")
    public void atualizar_anotacao_cenario2() throws Exception {
        var dtoRequisicao = new DtoAnotacoesCadastroRequisicao(
                null, "titulo alterado", "conteudo alterado"
        );

        EntityNotFoundException ex = new EntityNotFoundException("Anotacao de id 20 nao existe");
        Mockito.doThrow(ex).when(this.anotacaoService).atualizarAnotacao(20l, dtoRequisicao);

        var jsonRequisicaoAlteracao = this.dtoCadastroAnotacao.write(dtoRequisicao).getJson();
        var response = this.mvc.perform(
                put("/anotacoes/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequisicaoAlteracao)
        ).andReturn().getResponse();

        DtoErro dtoErroResponse = new DtoErro("Anotacao inexistente", "Anotacao de id 20 nao existe", "", "404");
        String jsonReponseEsperado = this.dtoErro.write(dtoErroResponse).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonReponseEsperado);
    }

    @Test
    @DisplayName("Deveria retornar código 200 e Json da anotação quando for encontrada")
    public void obter_anotacao_cenario2() throws Exception {

        var dtoRespostaService = new DtoAnotacoesResposta(1l, "titulo1", "conteudo1", LocalDateTime.now());
        when(this.anotacaoService.obterAnotacao(1l)).thenReturn(dtoRespostaService);

        var response = this.mvc.perform(
                                get("/anotacoes/1")
                        ).andReturn().getResponse();

        var jsonEsperado = this.dtoRespostaAnotacoes.write(dtoRespostaService).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @DisplayName("Deveria retornar código 404 e Json de erro quando buscar anotacao inexistente")
    public void obter_anotacao_cenario1() throws Exception {

        var ex = new EntityNotFoundException("Anotacao de id 20 nao existe");
        doThrow(ex).when(this.anotacaoService).obterAnotacao(20l);

        var response = this.mvc.perform(
                get("/anotacoes/20")
        ).andReturn().getResponse();

        DtoErro dtoErroResponse = new DtoErro("Anotacao inexistente", "Anotacao de id 20 nao existe", "", "404");
        String jsonReponseEsperado = this.dtoErro.write(dtoErroResponse).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonReponseEsperado);

    }

}
