package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.dto.ParticipanteRequestDTO;
import br.com.gestao_pelada.partida.application.dto.ParticipanteResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PresencaUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Participantes", description = "Confirmacao de presenca de jogadores em uma partida")
@RequestMapping("/api/v1/partidas/{partidaId}/participantes")
public interface ParticipanteApi {

    @Operation(
            summary = "Adiciona um participante a partida",
            description = "Confirma a participacao de um jogador em uma partida, podendo indicar se atuara como goleiro.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Participante adicionado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida ou jogador nao encontrado"),
                @ApiResponse(responseCode = "409", description = "Jogador ja e participante da partida")
            })
    @PostMapping
    ResponseEntity<ParticipanteResponseDTO> adicionar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId,
            @RequestBody @Valid ParticipanteRequestDTO request);

    @Operation(summary = "Lista os participantes de uma partida", description = "Retorna todos os jogadores confirmados em uma partida.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Participantes listados com sucesso")})
    @GetMapping
    ResponseEntity<List<ParticipanteResponseDTO>> listar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId);

    @Operation(
            summary = "Atualiza a presenca de um participante",
            description = "Marca ou desmarca a presenca de um jogador em uma partida.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Presenca atualizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida, jogador ou participante nao encontrado")
            })
    @PatchMapping("/{jogadorId}/presenca")
    ResponseEntity<ParticipanteResponseDTO> atualizarPresenca(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId,
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("jogadorId")
                    UUID jogadorId,
            @RequestBody @Valid PresencaUpdateDTO request);

    @Operation(
            summary = "Remove um participante da partida",
            description = "Remove a confirmacao de um jogador em uma partida.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Participante removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Partida, jogador ou participante nao encontrado")
            })
    @DeleteMapping("/{jogadorId}")
    ResponseEntity<Void> remover(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    UUID partidaId,
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("jogadorId")
                    UUID jogadorId);
}
