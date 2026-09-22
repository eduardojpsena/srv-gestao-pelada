package br.com.gestao_pelada.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testa o fluxo completo de autenticacao (registro, login, refresh) e a
 * criacao de pelada e autorizacao contextual nos endpoints protegidos por JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveNegarAcessoAEndpointProtegidoSemToken() throws Exception {
        mockMvc.perform(get("/api/v1/peladas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRegistrarFazerLoginERenovarToken() throws Exception {
        String email = "usuario.teste@pelada.com";
        String registerBody = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("nome", "Usuario Teste");
            put("email", email);
            put("senha", "senha123");
        }});

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.cadastroConcluido").value(true));

        String loginBody = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("email", email);
            put("senha", "senha123");
        }});

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();
        String refreshToken = objectMapper.readTree(loginResponse).get("refreshToken").asText();

        // Acessa endpoint protegido com o access token emitido no login.
        mockMvc.perform(get("/api/v1/peladas")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        // Renova os tokens usando o refresh token.
        String refreshBody = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("refreshToken", refreshToken);
        }});

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void qualquerUsuarioRegistradoPodeCriarPeladaETornaSeAdmin() throws Exception {
        registrarUsuario("criador@pelada.com");
        String tokenCriador = logarERetornarAccessToken("criador@pelada.com");

        String peladaBody = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("nome", "Pelada de Teste");
        }});

        mockMvc.perform(post("/api/v1/peladas")
                        .header("Authorization", "Bearer " + tokenCriador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peladaBody))
                .andExpect(status().isCreated());
    }

    private void registrarUsuario(String email) throws Exception {
        String body = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("nome", "Usuario " + email);
            put("email", email);
            put("senha", "senha123");
        }});
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    private String logarERetornarAccessToken(String email) throws Exception {
        String body = objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
            put("email", email);
            put("senha", "senha123");
        }});
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("accessToken").asText();
    }
}
