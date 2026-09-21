package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaStatusUpdateDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Partidas", description = "Cadastro e gestao de partidas de uma pelada")
public interface PartidaApi {

    @Operation(
            summary = "Cadastra uma nova partida",
            description = "Cria uma nova partida vinculada a uma pelada existente.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Pelada nao encontrada")
            })
    @PostMapping("/api/v1/peladas/{peladaId}/partidas")
    ResponseEntity<PartidaResponseDTO> criar(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    UUID peladaId,
            @RequestBody @Valid PartidaRequestDTO request);

    @Operation(summary = "Lista as partidas de uma pelada", description = "Retorna as partidas de uma pelada de forma paginada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Partidas listadas com sucesso")})
    @GetMapping("/api/v1/peladas/{peladaId}/partidas")
    ResponseEntity<PageResponse<PartidaResponseDTO>> listarPorPelada(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    UUID peladaId,
            Pageable pageable);

    @Operation(summary = "Busca uma partida por id", description = "Retorna os dados de uma partida a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Partida encontrada"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @GetMapping("/api/v1/partidas/{id}")
    ResponseEntity<PartidaResponseDTO> buscarPorId(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("id") UUID id);

    @Operation(summary = "Atualiza uma partida", description = "Atualiza os dados de uma partida existente.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @PutMapping("/api/v1/partidas/{id}")
    ResponseEntity<PartidaResponseDTO> atualizar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("id") UUID id,
            @RequestBody @Valid PartidaRequestDTO request);

    @Operation(
            summary = "Atualiza o status de uma partida",
            description =
                    """
                    Atualiza o status de uma partida existente.

                    **Campos do payload:**
                    - `status`: {"AGENDADA", "EM_ANDAMENTO", "FINALIZADA", "CANCELADA"}
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @PatchMapping("/api/v1/partidas/{id}/status")
    ResponseEntity<PartidaResponseDTO> atualizarStatus(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("id") UUID id,
            @RequestBody @Valid PartidaStatusUpdateDTO request);

    @Operation(summary = "Remove uma partida", description = "Exclui uma partida cadastrada a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Partida removida com sucesso"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @DeleteMapping("/api/v1/partidas/{id}")
    ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("id") UUID id);
}
