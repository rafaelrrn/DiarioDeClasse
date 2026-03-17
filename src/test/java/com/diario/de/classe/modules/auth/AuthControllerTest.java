package com.diario.de.classe.modules.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração do AuthController.
 *
 * Usa @SpringBootTest com H2 em memória (perfil "test") para testar o fluxo
 * completo de autenticação: registro, login e senha inválida.
 *
 * MockMvc opera no nível de servlet — o context-path (/api) não é incluído
 * nos caminhos, portanto usa-se /v1/auth/... diretamente.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        userRepository.deleteAll();
    }

    // -------------------------------------------------------------------------
    // register
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("POST /v1/auth/register — registro de usuário")
    class RegisterTest {

        @Test
        @DisplayName("Deve registrar usuário com sucesso e retornar mensagem de confirmação")
        void deveRegistrar_quandoDadosValidos() throws Exception {
            Map<String, String> payload = Map.of(
                    "nome", "Maria Professora",
                    "email", "maria@escola.com",
                    "senha", "senha123",
                    "role", "PROFESSOR"
            );

            mockMvc.perform(post("/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Usuário registrado com sucesso"));
        }
    }

    // -------------------------------------------------------------------------
    // login
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("POST /v1/auth/login — autenticação")
    class LoginTest {

        @BeforeEach
        void criarUsuario() throws Exception {
            // Registra um usuário para usar nos testes de login
            Map<String, String> payload = Map.of(
                    "nome", "Admin Teste",
                    "email", "admin@escola.com",
                    "senha", "senhaCorreta",
                    "role", "ADMINISTRADOR"
            );
            mockMvc.perform(post("/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)));
        }

        @Test
        @DisplayName("Deve retornar 200 e definir cookie auth_token quando credenciais são válidas")
        void deveRetornarCookie_quandoCredenciaisValidas() throws Exception {
            Map<String, String> payload = Map.of(
                    "email", "admin@escola.com",
                    "senha", "senhaCorreta"
            );

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists("auth_token"))
                    .andExpect(cookie().httpOnly("auth_token", true))
                    .andExpect(jsonPath("$.message").value("Login realizado com sucesso"));
        }

        @Test
        @DisplayName("Deve retornar erro quando a senha está incorreta")
        void deveRetornarErro_quandoSenhaIncorreta() throws Exception {
            Map<String, String> payload = Map.of(
                    "email", "admin@escola.com",
                    "senha", "senhaErrada"
            );

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().is5xxServerError());
        }

        @Test
        @DisplayName("Deve retornar erro quando o email não existe")
        void deveRetornarErro_quandoEmailNaoExiste() throws Exception {
            Map<String, String> payload = Map.of(
                    "email", "naoexiste@escola.com",
                    "senha", "qualquersenha"
            );

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().is5xxServerError());
        }
    }

    // -------------------------------------------------------------------------
    // me
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /v1/auth/me — dados do usuário autenticado")
    class MeTest {

        @Test
        @DisplayName("Deve retornar dados do usuário quando token JWT válido está no cookie")
        void deveRetornarDadosUsuario_quandoAutenticado() throws Exception {
            // Registra e faz login para obter o cookie
            Map<String, String> registro = Map.of(
                    "nome", "Coordenador Teste",
                    "email", "coord@escola.com",
                    "senha", "senha456",
                    "role", "COORDENADOR"
            );
            mockMvc.perform(post("/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registro)));

            Map<String, String> login = Map.of(
                    "email", "coord@escola.com",
                    "senha", "senha456"
            );
            var loginResult = mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login)))
                    .andReturn();

            Cookie authCookie = loginResult.getResponse().getCookie("auth_token");

            mockMvc.perform(get("/v1/auth/me").cookie(authCookie))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.email").value("coord@escola.com"))
                    .andExpect(jsonPath("$.data.nome").value("Coordenador Teste"))
                    .andExpect(jsonPath("$.data.role").value("COORDENADOR"));
        }

        @Test
        @DisplayName("Deve retornar erro quando nenhum cookie é enviado")
        void deveRetornarErro_quandoSemCookie() throws Exception {
            mockMvc.perform(get("/v1/auth/me"))
                    .andExpect(status().is5xxServerError());
        }
    }
}
