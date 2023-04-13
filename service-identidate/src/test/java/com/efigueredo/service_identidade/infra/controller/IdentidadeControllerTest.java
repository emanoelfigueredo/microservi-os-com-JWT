package com.efigueredo.service_identidade.infra.controller;

import com.efigueredo.service_identidade.infra.conf.exception.DtoErro;
import com.efigueredo.service_identidade.infra.conf.exception.IdentityException;
import com.efigueredo.service_identidade.service.TokenJwt;
import com.efigueredo.service_identidade.service.TokenJwtService;
import com.efigueredo.service_identidade.service.UsuarioService;
import com.efigueredo.service_identidade.service.dto.DtoAutenticacao;
import com.efigueredo.service_identidade.service.dto.requisicao.DtoRegistroRequisicao;
import com.efigueredo.service_identidade.util.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IdentidadeControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenJwtService tokenJwtService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private JacksonTester<DtoRegistroRequisicao> dtoRegistro;

    @Autowired
    private JacksonTester<DtoAutenticacao> dtoAutenticacao;

    @Autowired
    private JacksonTester<TokenJwt> tokenJwtJson;

    @Autowired
    private JacksonTester<DtoErro> dtoErro;

    @Test
    @DisplayName("Deveria retornar código 200 quando registro de usuário ocorreu com sucesso")
    public void registrar_cenario1() throws Exception {
        DtoRegistroRequisicao dto = new DtoRegistroRequisicao(null, "nome", "username", "senha123");
        when(this.usuarioService.salvarUsuario(dto)).thenReturn(null);
        var response = this.mvc.perform(
                            post("/identidade/registrar")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(this.dtoRegistro.write(dto).getJson())
                        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria retornar código 200 quando validação de token JWT ocorrer com sucesso.")
    public void validar_cenario1() throws Exception {
        String token = this.jwtUtil.gerarToken("admin", (100 * 60 * 30)).getToken();
        doNothing().when(this.tokenJwtService).validarToken(token);
        var response = this.mvc.perform(
                get("/identidade/validar")
                        .param("token", token)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria retornar código 401 quando token JWT enviado para validação for inválido.")
    public void validar_cenario2() throws Exception {

        String tokenInvalido = "tokeninvalido.tokeninvalido.tokenInvalido";
        IdentityException excecao = new IdentityException("Falha na autenticacao", "Token JWT invalido", "", "401");
        doThrow(excecao).when(this.tokenJwtService).validarToken(tokenInvalido);

        var response = this.mvc.perform(
                get("/identidade/validar")
                        .param("token", tokenInvalido)
        ).andReturn().getResponse();

        DtoErro dtoErro = new DtoErro("Falha na autenticacao", "Token JWT invalido", "", "401");
        String jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @DisplayName("Deveria retornar código 401 quando token JWT enviado para validação estiver expirado.")
    public void validar_cenario3() throws Exception {

        String tokenExpirado = this.jwtUtil.gerarToken("admin", (1)).getToken();
        IdentityException excecao = new IdentityException("Falha na autenticacao", "Token JWT expirado", "", "401");
        doThrow(excecao).when(this.tokenJwtService).validarToken(tokenExpirado);

        var response = this.mvc.perform(
                get("/identidade/validar")
                        .param("token", tokenExpirado)
        ).andReturn().getResponse();

        DtoErro dtoErro = new DtoErro("Falha na autenticacao", "Token JWT expirado", "", "401");
        String jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @DisplayName("Deveria retornar código 200 e token JWT quando autenticação ocorrer com sucesso.")
    public void autenticar_cenario1() throws Exception {

        DtoAutenticacao dto = new DtoAutenticacao("admin", "admin123");
        TokenJwt token = this.jwtUtil.gerarToken("admin", (100 * 60 * 30));

        doNothing().when(this.usuarioService).autenticar(dto);
        when(this.tokenJwtService.gerarToken(dto.username())).thenReturn(token);

        var response = this.mvc.perform(
                post("/identidade/autenticar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoAutenticacao.write(dto).getJson())
        ).andReturn().getResponse();

        String tokenJwtJson = this.tokenJwtJson.write(token).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(tokenJwtJson);

    }

    @Test
    @DisplayName("Deveria retornar código 401 quando autenticação falhar.")
    public void autenticar_cenario2() throws Exception {

        DtoAutenticacao dto = new DtoAutenticacao("admin", "admin123");
        TokenJwt token = this.jwtUtil.gerarToken("admin", (100 * 60 * 30));

        IdentityException excecao = new IdentityException("Falha na autenticacao", "Credenciais invalidas", "", "401");
        doThrow(excecao).when(this.usuarioService).autenticar(dto);
        when(this.tokenJwtService.gerarToken(dto.username())).thenReturn(token);

        var response = this.mvc.perform(
                post("/identidade/autenticar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.dtoAutenticacao.write(dto).getJson())
        ).andReturn().getResponse();

        DtoErro dtoErro = new DtoErro("Falha na autenticacao", "Credenciais invalidas", "", "401");
        String jsonEsperado = this.dtoErro.write(dtoErro).getJson();

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

}