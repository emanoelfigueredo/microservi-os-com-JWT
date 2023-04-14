package com.efigueredo.service_identidade.infra.controller;

import com.efigueredo.service_identidade.anotacoes.Admin;
import com.efigueredo.service_identidade.anotacoes.User;
import com.efigueredo.service_identidade.infra.conf.exception.DtoErro;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.TokenJwtService;
import com.efigueredo.service_identidade.service.UsuarioService;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoSenha;
import com.efigueredo.service_identidade.service.dto.resposta.DtoUsuarioResposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.efigueredo.service_identidade.domain.Usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class UsuariosControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenJwtService tokenJwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DtoUsuarioResposta> dtoRespostaUsuario;

    @Autowired
    private JacksonTester<DtoErro> dtoErro;

    @Autowired
    private JacksonTester<DtoSenha> dtoSenha;

    @Autowired
    private JacksonTester<CustomUserDetails> JsonCustomUsuario;

    @Autowired
    private JacksonTester<Usuario> JsonUsuario;

    @Autowired
    private JacksonTester<DtoRegistroRequisicao> dtoRegistro;

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar código 200 e o dto de usuário pelo seu id caso ele exista")
    public void obterUsuarioPorId_cenario1() throws Exception {

        DtoUsuarioResposta dto = new DtoUsuarioResposta("emanoel", "efigueredo", true);

        when(this.usuarioService.obterUsuarioPorId(1l)).thenReturn(dto);

        var response = this.mockMvc.perform(
                get("/usuarios/id/1")
        ).andReturn().getResponse();

        var jsonEsperado = this.dtoRespostaUsuario.write(dto).getJson();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar erro 404 quando obter usuário de id inexistente")
    public void obterUsuarioPorId_cenario2() throws Exception {

        IdentityException excecao = new IdentityException("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        doThrow(excecao).when(this.usuarioService).obterUsuarioPorId(1l);

        var response = this.mockMvc.perform(
                get("/usuarios/id/1")
        ).andReturn().getResponse();

        var dtoErro = new DtoErro("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        var jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar código 200 e o dto de usuário pelo seu username caso ele exista")
    public void obterUsuarioPorUsername_cenario1() throws Exception {

        String username = "efigueredo";

        DtoUsuarioResposta dto = new DtoUsuarioResposta("emanoel", username, true);

        when(this.usuarioService.obterUsuarioPorUsername(username)).thenReturn(dto);

        var response = this.mockMvc.perform(
                get("/usuarios/username/" + username)
        ).andReturn().getResponse();

        var jsonEsperado = this.dtoRespostaUsuario.write(dto).getJson();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar erro 404 quando obter usuário de username inexistente")
    public void obterUsuarioPorUsername_cenario2() throws Exception {

        String username = "efigueredo";

        IdentityException excecao = new IdentityException("Valor invalido", "Usuario de username '" + username + "' nao existe no sistema", "", "404");
        doThrow(excecao).when(this.usuarioService).obterUsuarioPorUsername(username);

        var response = this.mockMvc.perform(
                get("/usuarios/username/" + username)
        ).andReturn().getResponse();

        var dtoErro = new DtoErro("Valor invalido", "Usuario de username '" + username + "' nao existe no sistema", "", "404");
        var jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @User
    @DisplayName("USER - Deveria retornar código 200 e as proprias credenciais")
    public void obterSuasCredencias_cenario1() throws Exception {

        Usuario usuario = new Usuario(null, "administrador", "admin", "admin123", "ROLE_ADMINISTRADOR,ROLE_USUARIO", true);
        CustomUserDetails customUserDetails = new CustomUserDetails(usuario);
        DtoSenha dtoSenha = new DtoSenha("admin123");

        when(this.usuarioService.obterProprioUsuario("admin123")).thenReturn(customUserDetails);

        var response = this.mockMvc.perform(
                post("/usuarios/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoSenha.write(dtoSenha).getJson())
        ).andReturn().getResponse();

        var jsonEsperado = this.JsonCustomUsuario.write(customUserDetails).getJson();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar código 204 quando deletar usuario for efetuado com sucesso")
    public void deletarUsuario_cenario1() throws Exception {
        doNothing().when(this.usuarioService).removerUsuario(1l);
        var response = this.mockMvc.perform(
                delete("/usuarios/1")
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(204);
    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar código 404 quando deletar usuario com id inexistente")
    public void deletarUsuario_cenario2() throws Exception {
        var excecao = new IdentityException("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        doThrow(excecao).when(this.usuarioService).removerUsuario(1l);
        var response = this.mockMvc.perform(
                delete("/usuarios/1")
        ).andReturn().getResponse();

        var dtoErro = new DtoErro("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        var jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(jsonEsperado).isEqualTo(response.getContentAsString());
    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar código 200 quando atualizar usuario for efetuado com sucesso")
    public void atualizarUsuario_cenario1() throws Exception {

        Usuario usuario = new Usuario(1l, "nome-alterado", "username-alterado", "senha-alterada", "ROLE_ADMINISTRADOR,ROLE_USUARIO", true);
        var dtoRegistro = new DtoRegistroRequisicao(null, "nome-alterado", "username-alterado", "senha-alterada");

        doReturn(usuario).when(this.usuarioService).alterarUsuario(1l, dtoRegistro);
        var response = this.mockMvc.perform(
                put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoRegistro.write(dtoRegistro).getJson())
        ).andReturn().getResponse();

        var jsonEsperado = this.JsonUsuario.write(usuario).getJson();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @Admin
    @DisplayName("ADMIN - Deveria retornar erro 404 quando atualizar usuário de id inexistente")
    public void atualizarUsuario_cenario2() throws Exception {

        var dtoRegistro = new DtoRegistroRequisicao(null, "nome-alterado", "username-alterado", "senha-alterada");
        IdentityException excecao = new IdentityException("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        doThrow(excecao).when(this.usuarioService).alterarUsuario(1l, dtoRegistro);

        var response = this.mockMvc.perform(
                put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoRegistro.write(dtoRegistro).getJson())
        ).andReturn().getResponse();

        var dtoErro = new DtoErro("Valor invalido", "Usuario de id 1 nao existe no sistema", "", "404");
        var jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @User
    @DisplayName("USER - Deveria retornar código 200 quando atualizar as proprias credenciais for efetuado com sucesso")
    public void alterarCredenciaisProprias_cenario1() throws Exception {

        Usuario usuario = new Usuario(1l, "nome-alterado", "username-alterado", "senha-alterada", "ROLE_ADMINISTRADOR,ROLE_USUARIO", true);
        var dtoRegistro = new DtoRegistroRequisicao(null, "nome-alterado", "username-alterado", "senha-alterada");

        doReturn(usuario).when(this.usuarioService).alterarPropriasCredenciais(dtoRegistro);
        var response = this.mockMvc.perform(
                put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoRegistro.write(dtoRegistro).getJson())
        ).andReturn().getResponse();

        var jsonEsperado = this.JsonUsuario.write(usuario).getJson();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @User
    @DisplayName("USER - Deveria retornar código 200 quando desativar proprio usuario for efetuado com sucesso")
    public void desativarProprioUsuario_cenario1() throws Exception {

        doNothing().when(this.usuarioService).desativarUsuarioProprio();
        var response = this.mockMvc.perform(
                put("/usuarios/desativar")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

    }

    @Test
    @User
    @DisplayName("USER - Deveria retornar código 200 quando ativar proprio usuario for efetuado com sucesso")
    public void ativarProprioUsuario_cenario2() throws Exception {

        doNothing().when(this.usuarioService).desativarUsuarioProprio();
        var response = this.mockMvc.perform(
                put("/usuarios/ativar")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

    }

    @Test
    @DisplayName("Deveria retornar código 200 e a role do usuário pelo token JWT")
    public void obterRolePorTokenJwt_cenario1() throws Exception {

        doReturn("ROLE_USUARIO").when(this.usuarioService).obterRolePorTokenJwt("tokenJWT");
        var response = this.mockMvc.perform(
                post("/usuarios/role/tokenJWT")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("ROLE_USUARIO");

    }

}