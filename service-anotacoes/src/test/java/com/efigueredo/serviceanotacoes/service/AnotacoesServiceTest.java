package com.efigueredo.serviceanotacoes.service;

import com.efigueredo.serviceanotacoes.domain.Anotacao;
import com.efigueredo.serviceanotacoes.service.dto.DtoAnotacoesResposta;
import com.efigueredo.serviceanotacoes.service.dto.requisicao.DtoAnotacoesCadastroRequisicao;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ComponentScan("com.efigueredo.serviceanotacoes")
class AnotacoesServiceTest {

    @Autowired
    private AnotacoesService service;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria retornar um DTO de resposta buscando pelo ID")
    public void obter_anotacao_cenario1() {
        Anotacao anotacao = this.cadastrarAnotacao("titulo1", "conteudo1");
        DtoAnotacoesResposta dtoEsperado = new DtoAnotacoesResposta(anotacao.getId(), anotacao.getTitulo(), anotacao.getConteudo(), anotacao.getMomento());
        DtoAnotacoesResposta dtoAnotacoesResposta = this.service.obterAnotacao(anotacao.getId());
        assertThat(dtoAnotacoesResposta).isEqualTo(dtoEsperado);
    }

    @Test
    @DisplayName("Deveria criar uma anotacao e persisti-la no banco, devolvendo um dto de resposta")
    public void criar_anotacao_cenario1() {
        var dtoCadastro = new DtoAnotacoesCadastroRequisicao(null, "titulo1", "conteudo1");
        var dtoAnotacoesResposta = this.service.criarAnotacao(dtoCadastro);

        var dtoEsperado = new DtoAnotacoesResposta(dtoAnotacoesResposta.getId(), dtoCadastro.getTitulo(), dtoCadastro.getConteudo(), dtoAnotacoesResposta.getMomento());
        assertThat(dtoAnotacoesResposta).isEqualTo(dtoEsperado);
    }

    @Test
    @DisplayName("Deveria remover uma anotação e não retornar nada")
    public void remover_anotacao_cenario1() {
        Anotacao anotacao = this.cadastrarAnotacao("titulo1", "conteudo1");
        Long id = anotacao.getId();
        this.service.removerAnotacao(id);
        Anotacao anotacaoEncontrada = this.em.find(Anotacao.class, id);
        assertNull(anotacaoEncontrada);
    }

    @Test
    @DisplayName("Deveria lançar exececao quando remover anotacao com id invalido")
    public void remover_anotacao_cenario2() {
        assertThrows(EntityNotFoundException.class, () -> this.service.removerAnotacao(20l));
    }

    @Test
    @DisplayName("Deveria atualizar a anotação e devolver um DTO de resposta contendo seus dados")
    public void atualizar_anotacao_cenario1() {
        Anotacao anotacao = this.cadastrarAnotacao("titulo1", "conteudo1");
        Long id = anotacao.getId();
        DtoAnotacoesCadastroRequisicao dto = new DtoAnotacoesCadastroRequisicao(null, "titulo-atualizado", "conteudo-atualizado");
        DtoAnotacoesResposta dtoAnotacaoAtualizada = this.service.atualizarAnotacao(id, dto);
        DtoAnotacoesResposta dtoEsperado = new DtoAnotacoesResposta(id, "titulo-atualizado", "conteudo-atualizado", anotacao.getMomento());
        assertThat(dtoAnotacaoAtualizada).isEqualTo(dtoEsperado);
    }

    @Test
    @DisplayName("Deveria lançar exececao quando atualizar anotacao com id invalido")
    public void atualizar_anotacao_cenario2() {
        var dto = new DtoAnotacoesCadastroRequisicao(null, "titulo", "conteudo");
        assertThrows(EntityNotFoundException.class, () -> this.service.atualizarAnotacao(20l, dto));
    }

    private Anotacao cadastrarAnotacao(String titulo, String conteudo) {
        Anotacao anotacao = new Anotacao(null, titulo, conteudo, LocalDateTime.now());
        return this.em.persist(anotacao);
    }

}