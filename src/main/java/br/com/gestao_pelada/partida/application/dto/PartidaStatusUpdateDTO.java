package br.com.gestao_pelada.partida.application.dto;

import br.com.gestao_pelada.partida.domain.StatusPartida;
import jakarta.validation.constraints.NotNull;

public record PartidaStatusUpdateDTO(
        @NotNull(message = "O status deve ser informado")
        StatusPartida status
) {
}
