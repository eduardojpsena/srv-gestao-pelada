package br.com.gestao_pelada.sorteio.controller;

import br.com.gestao_pelada.sorteio.model.dto.SorteioRequestDTO;
import br.com.gestao_pelada.time.model.dto.TimeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Sorteio", description = "Sorteio de times por potes, estrelas ou avulso")
@RequestMapping("/api/v1/partidas/{partidaId}/sorteio")
public interface SorteioApi {

    @Operation(
            summary = "Sorteia os times de uma partida",
            description =
                    """
                    Realiza o sorteio dos times de uma partida a partir dos jogadores confirmados.

                    **Campos do payload:**
                    - `tipo`: {"POTES", "ESTRELAS", "AVULSO"}
                    - `numeroTimes`: quantidade de times a serem formados, minimo `2`
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Times sorteados com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Partida nao encontrada")
            })
    @PostMapping
    ResponseEntity<List<TimeResponseDTO>> sortear(
            @Parameter(description = "Identificador da partida", required = true) @PathVariable("partidaId")
                    Long partidaId,
            @RequestBody @Valid SorteioRequestDTO request);
}

