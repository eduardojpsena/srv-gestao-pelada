package br.com.gestao_pelada.time.controller;

import br.com.gestao_pelada.time.model.dto.TimeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Times", description = "Times formados para uma partida")
@RequestMapping("/api/v1/partidas/{partidaId}/times")
public interface TimeApi {

    @Operation(summary = "Lista os times de uma partida", description = "Retorna os times formados para uma partida, incluindo seus jogadores.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Times listados com sucesso")})
    @GetMapping
    ResponseEntity<List<TimeResponseDTO>> listar(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    Long partidaId);

    @Operation(
            summary = "Remove os times de uma partida",
            description = "Exclui todos os times formados para uma partida, permitindo um novo sorteio.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Times removidos com sucesso"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @DeleteMapping
    ResponseEntity<Void> remover(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    Long partidaId);
}

