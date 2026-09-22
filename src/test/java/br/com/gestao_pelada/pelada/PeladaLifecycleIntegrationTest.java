package br.com.gestao_pelada.pelada;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Cobre o fluxo ponta a ponta da API: cadastro de jogadores, criacao de pelada,
 * vinculo de jogadores, criacao de partida, confirmacao de presenca, sorteio de
 * times e registro de eventos/estatisticas.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PeladaLifecycleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenOrganizador;

    @Test
    void devePercorrerTodoOFluxoDeUmaPeladaAteAsEstatisticas() throws Exception {
        tokenOrganizador = registrarELogar("organizador.fluxo@pelada.com");

        String peladaId = criarPelada();

        String jogador1Id = provisionarJogador(peladaId, "Jogador Um", "jogador1@pelada.com");
        String jogador2Id = provisionarJogador(peladaId, "Jogador Dois", "jogador2@pelada.com");
        String jogador3Id = provisionarJogador(peladaId, "Jogador Tres", "jogador3@pelada.com");
        String jogador4Id = provisionarJogador(peladaId, "Jogador Quatro", "jogador4@pelada.com");

        mockMvc.perform(get("/api/v1/peladas/" + peladaId + "/jogadores")
                        .header("Authorization", "Bearer " + tokenOrganizador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        String partidaId = criarPartida(peladaId);

        adicionarParticipante(partidaId, jogador1Id);
        adicionarParticipante(partidaId, jogador2Id);
        adicionarParticipante(partidaId, jogador3Id);
        adicionarParticipante(partidaId, jogador4Id);

        // Sorteia times pelo criterio de estrelas (balanceamento por nota).
        String sorteioBody = objectMapper.writeValueAsString(Map.of("tipo", "ESTRELAS"));
        String sorteioResponse = mockMvc.perform(post("/api/v1/partidas/" + partidaId + "/sorteio")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sorteioBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        JsonNode times = objectMapper.readTree(sorteioResponse);
        String timeId = times.get(0).get("id").asText();

        // Confirma que os times foram persistidos e podem ser listados.
        mockMvc.perform(get("/api/v1/partidas/" + partidaId + "/times")
                        .header("Authorization", "Bearer " + tokenOrganizador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        // Registra um gol para o primeiro jogador do primeiro time.
        String jogadorDoTimeId = times.get(0).get("jogadores").get(0).get("jogadorId").asText();
        String eventoBody = objectMapper.writeValueAsString(new LinkedHashMap<>() {{
            put("timeId", timeId);
            put("jogadorId", jogadorDoTimeId);
            put("tipo", "GOL");
            put("minuto", 15);
        }});

        mockMvc.perform(post("/api/v1/partidas/" + partidaId + "/eventos")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventoBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("GOL"));

        // Confirma presenca do artilheiro para que ele conte nas estatisticas.
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/api/v1/partidas/" + partidaId + "/participantes/" + jogadorDoTimeId + "/presenca")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("presente", true))))
                .andExpect(status().isOk());

        MvcResult rankingResult = mockMvc.perform(get("/api/v1/peladas/" + peladaId + "/estatisticas/artilheiros")
                        .header("Authorization", "Bearer " + tokenOrganizador))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode ranking = objectMapper.readTree(rankingResult.getResponse().getContentAsString());
        assertThat(ranking.isArray()).isTrue();
        assertThat(ranking.get(0).get("jogadorId").asText()).isEqualTo(jogadorDoTimeId);
        assertThat(ranking.get(0).get("gols").asLong()).isEqualTo(1L);
    }

    private String registrarELogar(String email) throws Exception {
        String registerBody = objectMapper.writeValueAsString(new LinkedHashMap<>() {{
            put("nome", "Usuario " + email);
            put("email", email);
            put("senha", "senha123");
        }});
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated());

        String loginBody = objectMapper.writeValueAsString(Map.of("email", email, "senha", "senha123"));
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("accessToken").asText();
    }

    private String provisionarJogador(String peladaId, String nome, String email) throws Exception {
        String body = objectMapper.writeValueAsString(new LinkedHashMap<>() {{
            put("nome", nome);
            put("email", email);
            put("senhaPadrao", "senha123");
        }});
        String response = mockMvc.perform(post("/api/v1/peladas/" + peladaId + "/membros/provisionar")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String usuarioId = objectMapper.readTree(response).get("usuarioId").asText();
        String jogadores = mockMvc.perform(get("/api/v1/peladas/" + peladaId + "/jogadores")
                        .header("Authorization", "Bearer " + tokenOrganizador))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        for (JsonNode jogador : objectMapper.readTree(jogadores)) {
            if (jogador.get("nomeJogador").asText().equals(nome)) {
                return jogador.get("jogadorId").asText();
            }
        }
        throw new AssertionError("Jogador nao encontrado para usuario " + usuarioId);
    }

    private String criarPelada() throws Exception {
        String body = objectMapper.writeValueAsString(new LinkedHashMap<>() {{
            put("nome", "Pelada de Integracao");
            put("diaSemana", "SABADO");
        }});
        String response = mockMvc.perform(post("/api/v1/peladas")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }


    private String criarPartida(String peladaId) throws Exception {
        String body = objectMapper.writeValueAsString(new LinkedHashMap<>() {{
            put("data", java.time.LocalDate.now().plusDays(3).toString());
            put("numeroTimes", 2);
        }});
        String response = mockMvc.perform(post("/api/v1/peladas/" + peladaId + "/partidas")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }

    private void adicionarParticipante(String partidaId, String jogadorId) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("jogadorId", jogadorId));
        mockMvc.perform(post("/api/v1/partidas/" + partidaId + "/participantes")
                        .header("Authorization", "Bearer " + tokenOrganizador)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}