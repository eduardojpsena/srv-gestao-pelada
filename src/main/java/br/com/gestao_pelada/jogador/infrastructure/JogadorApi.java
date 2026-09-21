package br.com.gestao_pelada.jogador.infrastructure;

import br.com.gestao_pelada.jogador.application.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.application.dto.JogadorResponseDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Jogadores", description = "Cadastro e gestao de jogadores")
@RequestMapping("/api/v1/jogadores")
public interface JogadorApi {

    @Operation(
            summary = "Cadastra um novo jogador",
            description =
                    """
                    Cria um novo jogador na base.

                    **Campos do payload:**
                    - `posicao`: {"GOLEIRO", "ZAGUEIRO", "LATERAL", "MEIA", "ATACANTE"}
                    - `notaGeral`: nota entre `0.0` e `5.0`
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Jogador cadastrado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos")
            })
    @PostMapping
    ResponseEntity<JogadorResponseDTO> criar(@RequestBody @Valid JogadorRequestDTO request);

    @Operation(
            summary = "Busca um jogador por id",
            description = "Retorna os dados de um jogador a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Jogador encontrado"),
                @ApiResponse(responseCode = "404", description = "Jogador nao encontrado")
            })
    @GetMapping("/{id}")
    ResponseEntity<JogadorResponseDTO> buscarPorId(
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("id")
                    UUID id);

    @Operation(
            summary = "Lista os jogadores cadastrados",
            description = "Retorna os jogadores cadastrados de forma paginada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Jogadores listados com sucesso")})
    @GetMapping
    ResponseEntity<PageResponse<JogadorResponseDTO>> listar(Pageable pageable);

    @Operation(
            summary = "Atualiza um jogador",
            description =
                    """
                    Atualiza os dados cadastrais de um jogador existente.

                    **Campos do payload:**
                    - `posicao`: {"GOLEIRO", "ZAGUEIRO", "LATERAL", "MEIA", "ATACANTE"}
                    - `notaGeral`: nota entre `0.0` e `5.0`
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Jogador atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Jogador nao encontrado")
            })
    @PutMapping("/{id}")
    ResponseEntity<JogadorResponseDTO> atualizar(
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("id") UUID id,
            @RequestBody @Valid JogadorRequestDTO request);

    @Operation(summary = "Remove um jogador", description = "Exclui um jogador cadastrado a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Jogador removido com sucesso"),
                @ApiResponse(responseCode = "404", description = "Jogador nao encontrado")
            })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("id") UUID id);
}
