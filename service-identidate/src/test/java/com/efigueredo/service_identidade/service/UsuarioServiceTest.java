package com.efigueredo.service_identidade.service;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.dto.resposta.DtoUsuarioResposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ComponentScan("com.efigueredo.service_identidade")
class UsuarioServiceTest extends TestServiceUsuario {

    @Autowired
    private UsuarioService service;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Deveria registrar usuario com username nunca utilizado")
    public void salvar_usuario_cenario1() throws IdentityException {
        var dto = new DtoRegistroRequisicao(null, "nome", "username" ,"senha");
        DtoUsuarioResposta dtoUsuarioResposta = this.service.salvarUsuario(dto);
        var dtoRespostaEsperado = new DtoUsuarioResposta("nome", "username", true);
        assertThat(dtoUsuarioResposta).isEqualTo(dtoRespostaEsperado);
    }

    @Test
    @DisplayName("Deveria lançar exceção quando registrar usuario com username ja utilizado")
    public void salvar_usuario_cenario2() {
        super.cadastrarUsuario("jorge", "jorge", "jorge123", "ROLE_USUARIO", true, false);
        var dto = new DtoRegistroRequisicao(null, "nome", "jorge" ,"senha");
        assertThrows(IdentityException.class, () -> this.service.salvarUsuario(dto));
    }

    @DisplayName("Deveria retornar usuario quando buscado pelo id existente.")
    @Test
    public void obter_usuario_por_id_cenario1() throws IdentityException {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        var dtoUsuarioResposta = this.service.obterUsuarioPorId(usuario.getId());
        var dtoRespostaEsperado = new DtoUsuarioResposta(usuario);
        assertThat(dtoUsuarioResposta).isEqualTo(dtoRespostaEsperado);
    }

    @DisplayName("Deveria lançar exceção quando buscar usuário pelo id inexistente.")
    @Test
    public void obter_usuario_por_id_cenario2() {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        assertThrows(IdentityException.class, () -> this.service.obterUsuarioPorId(2l));
    }

    @DisplayName("Deveria retornar usuario quando buscado pelo username existente.")
    @Test
    public void obter_usuario_por_id_cenario3() throws IdentityException {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        var dtoUsuarioResposta = this.service.obterUsuarioPorUsername("paulo");
        var dtoRespostaEsperado = new DtoUsuarioResposta(usuario);
        assertThat(dtoUsuarioResposta).isEqualTo(dtoRespostaEsperado);
    }

    @DisplayName("Deveria lançar exceção quando buscar usuário pelo username inexistente.")
    @Test
    public void obter_usuario_por_id_cenario4() {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        assertThrows(IdentityException.class, () -> this.service.obterUsuarioPorUsername("joana"));
    }

    @DisplayName("Deveria remover logicamente o usuario de id existente")
    @Test
    public void remover_usuario_cenario1() throws IdentityException {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        this.service.removerUsuario(usuario.getId());
        Usuario usuarioResgatado = this.em.find(Usuario.class, usuario.getId());
        boolean usuarioEstaAtivado = usuarioResgatado.getActive();
        assertFalse(usuarioEstaAtivado);
    }

    @DisplayName("Deveria lançar exceção quando remover usuário com id inexistente")
    @Test
    public void remover_usuario_cenario2() {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        assertThrows(IdentityException.class, () -> this.service.removerUsuario(usuario.getId() + 1000));
    }

    @DisplayName("Deveria alterar usuario de id existente")
    @Test
    public void alterar_usuario_cenario1() throws IdentityException {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        var dtoAlteracao = new DtoRegistroRequisicao(null, "novo-nome", "novo-username", "nova-senha");
        Usuario usuarioAlterado = this.service.alterarUsuario(usuario.getId(), dtoAlteracao);
        Usuario usuarioResgatado = this.em.find(Usuario.class, usuario.getId());
        assertThat(usuarioAlterado).isEqualTo(usuarioResgatado);
    }

    @DisplayName("Deveria lançar exceção quando alterar usuario com id inexistente")
    @Test
    public void alterar_usuario_cenario2() {
        Usuario usuario = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        var dtoAlteracao = new DtoRegistroRequisicao(null, "novo-nome", "novo-username", "nova-senha");
        assertThrows(IdentityException.class, () -> this.service.alterarUsuario(usuario.getId() + 1000, dtoAlteracao));
    }

    @DisplayName("Deveria lançar exceção quando alterar usuario com username já utilizado")
    @Test
    public void alterar_usuario_cenario3() {
        Usuario usuario1 = this.cadastrarUsuario("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        Usuario usuario2 = this.cadastrarUsuario("Maria", "maria", "maria123", "ROLE_USUARIO", true, false);
        var dtoAlteracao = new DtoRegistroRequisicao(null, "novo-nome", "maria", "nova-senha");
        assertThrows(IdentityException.class, () -> this.service.alterarUsuario(usuario1.getId(), dtoAlteracao));
    }

    @DisplayName("Deveria desativar o proprio usuario logado quando não for administrador")
    @Test
    public void desativar_usuario_proprio_cenario1() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, false);
        this.service.desativarUsuarioProprio();
        Usuario usuarioRecuperado = this.em.find(Usuario.class, usuarioLogado.getId());
        assertFalse(usuarioRecuperado.getActive());
    }

    @DisplayName("Deveria lançar excecao quando desativar o proprio usuario com usuario logado sendo administrador")
    @Test
    public void desativar_usuario_proprio_cenario2() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_ADMINISTRADOR", true, false);
        assertThrows(IdentityException.class, () -> this.service.desativarUsuarioProprio());
    }

    @DisplayName("Deveria ativar o proprio usuario logado quando não for administrador")
    @Test
    public void ativar_usuario_proprio_cenario1() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", false, false);
        this.service.ativarUsuarioProprio();
        Usuario usuarioRecuperado = this.em.find(Usuario.class, usuarioLogado.getId());
        assertTrue(usuarioRecuperado.getActive());
    }

    @DisplayName("Deveria lançar excecao quando ativar o proprio usuario com usuario logado sendo administrador")
    @Test
    public void ativar_usuario_proprio_cenario2() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_ADMINISTRADOR", false, false);
        assertThrows(IdentityException.class, () -> this.service.ativarUsuarioProprio());
    }

    @DisplayName("Deveria alterar as credenciais do usuario logado")
    @Test
    public void alterar_proprias_credenciais_cenario1() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", false, false);
        var dtoAlteracao = new DtoRegistroRequisicao(null, "novo_nome", "novo_username", "nova_senha");
        Usuario usuarioAlterado = this.service.alterarPropriasCredenciais(dtoAlteracao);
        Usuario usuarioResgato = this.em.find(Usuario.class, usuarioLogado.getId());
        assertThat(usuarioAlterado).isEqualTo(usuarioResgato);
    }

    @DisplayName("Deveria lançar exceção quando alterar o usuario logado com username já utilizado")
    @Test
    public void alterar_proprias_credenciais_cenario2() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", false, false);
        this.cadastrarUsuario("Maria", "maria", "maria123", "ROLE_USUARIO", true, false);
        var dtoAlteracao = new DtoRegistroRequisicao(null, "novo_nome", "maria", "nova_senha");
        assertThrows(IdentityException.class, () -> this.service.alterarPropriasCredenciais(dtoAlteracao));
    }

    @DisplayName("Deveria retornar o proprio usuario logado com senha correta")
    @Test
    public void obter_proprio_usuario_logado_cenario1() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", true, true);
        CustomUserDetails usuarioObtido = this.service.obterProprioUsuario("paulo123");
        CustomUserDetails usuarioEsperado = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(usuarioObtido).isEqualTo(usuarioEsperado);
    }

    @DisplayName("Deveria lançar exceção quando buscar o proprio usuario com senha inserida incorreta")
    @Test
    public void obter_proprio_usuario_logado_cenario2() throws IdentityException {
        Usuario usuarioLogado = this.setarUsuarioLogado("Paulo", "paulo", "paulo123", "ROLE_USUARIO", false, true);
        assertThrows(IdentityException.class, () -> this.service.obterProprioUsuario("senha_incorreta"));

    }

}