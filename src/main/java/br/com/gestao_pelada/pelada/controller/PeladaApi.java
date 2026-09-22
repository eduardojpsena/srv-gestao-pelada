package br.com.gestao_pelada.pelada.controller;

import br.com.gestao_pelada.pelada.model.dto.AlterarPapelRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.DecisaoSolicitacaoRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.MembroRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.MembroResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.ProvisionarMembroRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.SolicitacaoEntradaResponseDTO;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Peladas", description = "Cadastro e gestao de peladas e seus jogadores")
@RequestMapping("/api/v1/peladas")
public interface PeladaApi {

    @Operation(
            summary = "Cadastra uma nova pelada",
            description =
                    """
                    Cria uma nova pelada. O usuario autenticado torna-se ADMIN automaticamente.

                    **Campos do payload:**
                    - `diaSemana`: {"SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"}
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Pelada cadastrada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos")
            })
    @PostMapping
    ResponseEntity<PeladaResponseDTO> criar(@RequestBody @Valid PeladaRequestDTO request);

    @Operation(summary = "Busca uma pelada por id", description = "Retorna os dados de uma pelada a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Pelada encontrada"),
                @ApiResponse(responseCode = "404", description = "Pelada nao encontrada")
            })
    @GetMapping("/{id}")
    ResponseEntity<PeladaResponseDTO> buscarPorId(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Lista as peladas cadastradas", description = "Retorna as peladas cadastradas de forma paginada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Peladas listadas com sucesso")})
    @GetMapping
    ResponseEntity<PageResponse<PeladaResponseDTO>> listar(Pageable pageable);

    @Operation(
            summary = "Atualiza uma pelada",
            description =
                    """
                    Atualiza os dados de uma pelada existente.

                    **Campos do payload:**
                    - `diaSemana`: {"SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"}
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Pelada atualizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Pelada nao encontrada")
            })
    @PutMapping("/{id}")
    ResponseEntity<PeladaResponseDTO> atualizar(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id,
            @RequestBody @Valid PeladaRequestDTO request);

    @Operation(summary = "Remove uma pelada", description = "Exclui uma pelada cadastrada a partir do seu identificador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Pelada removida com sucesso"),
                @ApiResponse(responseCode = "404", description = "Pelada nao encontrada")
            })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id);

    @Operation(
            summary = "Adiciona um jogador a pelada",
            description = "Vincula um jogador ja cadastrado a uma pelada, definindo nota e situacao de mensalista.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Jogador adicionado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Pelada ou jogador nao encontrado"),
                @ApiResponse(responseCode = "409", description = "Jogador ja vinculado a pelada")
            })
    @PostMapping("/{id}/jogadores")
    ResponseEntity<PeladaJogadorResponseDTO> adicionarJogador(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id,
            @RequestBody @Valid PeladaJogadorRequestDTO request);

    @Operation(summary = "Lista os jogadores de uma pelada", description = "Retorna todos os jogadores vinculados a uma pelada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Jogadores listados com sucesso")})
    @GetMapping("/{id}/jogadores")
    ResponseEntity<List<PeladaJogadorResponseDTO>> listarJogadores(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id);

    @Operation(
            summary = "Remove um jogador da pelada",
            description = "Desvincula um jogador de uma pelada a partir dos identificadores da pelada e do jogador.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Jogador removido da pelada com sucesso"),
                @ApiResponse(responseCode = "404", description = "Pelada, jogador ou vinculo nao encontrado")
            })
    @DeleteMapping("/{id}/jogadores/{jogadorId}")
    ResponseEntity<Void> removerJogador(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Identificador do jogador", required = true) @PathVariable("jogadorId")
                    Long jogadorId);

    @Operation(summary = "Vincula usuario registrado", description = "O ADMIN vincula por e-mail um usuario que concluiu o registro.")
    @PostMapping("/{id}/membros")
    ResponseEntity<MembroResponseDTO> adicionarMembro(
            @PathVariable("id") Long id, @RequestBody @Valid MembroRequestDTO request);

    @Operation(summary = "Provisiona jogador", description = "O ADMIN cria usuario e jogador com senha inicial. O cadastro fica pendente ate o usuario registrar uma nova senha.")
    @PostMapping("/{id}/membros/provisionar")
    ResponseEntity<MembroResponseDTO> provisionarMembro(
            @PathVariable("id") Long id, @RequestBody @Valid ProvisionarMembroRequestDTO request);

    @Operation(summary = "Solicita entrada na pelada", description = "Cria uma solicitacao pendente para o usuario autenticado.")
    @PostMapping("/{id}/solicitacoes-entrada")
    ResponseEntity<SolicitacaoEntradaResponseDTO> solicitarEntrada(@PathVariable("id") Long id);

    @Operation(summary = "Lista solicitacoes pendentes", description = "Disponivel para administradores da pelada.")
    @GetMapping("/{id}/solicitacoes-entrada")
    ResponseEntity<List<SolicitacaoEntradaResponseDTO>> listarSolicitacoes(@PathVariable("id") Long id);

    @Operation(summary = "Aprova ou recusa solicitacao", description = "Informe APROVADA ou RECUSADA. A aprovacao cria o membro JOGADOR.")
    @PatchMapping("/{id}/solicitacoes-entrada/{solicitacaoId}")
    ResponseEntity<SolicitacaoEntradaResponseDTO> decidirSolicitacao(
            @PathVariable("id") Long id,
            @PathVariable("solicitacaoId") Long solicitacaoId,
            @RequestBody @Valid DecisaoSolicitacaoRequestDTO request);

    @Operation(summary = "Altera papel do membro", description = "O ADMIN delega ADMIN, ORGANIZADOR ou JOGADOR dentro desta pelada.")
    @PatchMapping("/{id}/membros/{usuarioId}/papel")
    ResponseEntity<MembroResponseDTO> alterarPapel(
            @PathVariable("id") Long id,
            @PathVariable("usuarioId") Long usuarioId,
            @RequestBody @Valid AlterarPapelRequestDTO request);
}

