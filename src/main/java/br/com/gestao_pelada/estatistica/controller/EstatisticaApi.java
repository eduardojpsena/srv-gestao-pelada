package br.com.gestao_pelada.estatistica.controller;

import br.com.gestao_pelada.estatistica.model.dto.RankingJogadorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Estatisticas", description = "Rankings e estatisticas dos jogadores de uma pelada")
@RequestMapping("/api/v1/peladas/{peladaId}/estatisticas")
public interface EstatisticaApi {

    @Operation(
            summary = "Consulta o ranking completo da pelada",
            description = "Retorna o ranking geral dos jogadores de uma pelada, considerando todas as estatisticas registradas.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ranking listado com sucesso")})
    @GetMapping("/ranking")
    ResponseEntity<List<RankingJogadorDTO>> ranking(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    Long peladaId);

    @Operation(
            summary = "Consulta o ranking de artilheiros",
            description = "Retorna o ranking de jogadores ordenado pela quantidade de gols marcados na pelada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ranking de artilheiros listado com sucesso")})
    @GetMapping("/artilheiros")
    ResponseEntity<List<RankingJogadorDTO>> artilheiros(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    Long peladaId);

    @Operation(
            summary = "Consulta o ranking de assistencias",
            description = "Retorna o ranking de jogadores ordenado pela quantidade de assistencias na pelada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ranking de assistencias listado com sucesso")})
    @GetMapping("/assistencias")
    ResponseEntity<List<RankingJogadorDTO>> assistencias(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    Long peladaId);

    @Operation(
            summary = "Consulta o ranking de cartoes",
            description = "Retorna o ranking de jogadores ordenado pela quantidade de cartoes recebidos na pelada.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ranking de cartoes listado com sucesso")})
    @GetMapping("/cartoes")
    ResponseEntity<List<RankingJogadorDTO>> cartoes(
            @Parameter(description = "Identificador da pelada", required = true) @PathVariable("peladaId")
                    Long peladaId);
}

