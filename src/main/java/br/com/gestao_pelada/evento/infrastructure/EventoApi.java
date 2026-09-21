package br.com.gestao_pelada.evento.infrastructure;

import br.com.gestao_pelada.evento.application.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.application.dto.EventoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Eventos", description = "Eventos de partida: gols, assistencias e cartoes")
public interface EventoApi {

    @Operation(
            summary = "Registra um evento na partida",
            description =
                    """
                    Registra um evento ocorrido durante uma partida.

                    **Campos do payload:**
                    - `tipo`: {"GOL", "ASSISTENCIA", "CARTAO_AMARELO", "CARTAO_VERMELHO"}
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Evento registrado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida ou jogador nao encontrado")
            })
    @PostMapping("/api/v1/partidas/{partidaId}/eventos")
    ResponseEntity<EventoResponseDTO> registrar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId,
            @RequestBody @Valid EventoRequestDTO request);

    @Operation(summary = "Lista os eventos de uma partida", description = "Retorna todos os eventos registrados em uma partida.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Eventos listados com sucesso")})
    @GetMapping("/api/v1/partidas/{partidaId}/eventos")
    ResponseEntity<List<EventoResponseDTO>> listar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId);

    @Operation(summary = "Remove um evento", description = "Exclui um evento registrado a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Evento removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Evento nao encontrado")
            })
    @DeleteMapping("/api/v1/eventos/{id}")
    ResponseEntity<Void> remover(
            @Parameter(description = "Identificador do evento", required = true) @PathVariable("id") UUID id);
}
