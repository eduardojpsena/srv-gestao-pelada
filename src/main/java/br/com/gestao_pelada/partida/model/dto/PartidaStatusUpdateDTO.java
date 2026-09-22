package br.com.gestao_pelada.partida.model.dto;

import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.validation.constraints.NotNull;

public record PartidaStatusUpdateDTO(
        @NotNull(message = "O status deve ser informado")
        StatusPartida status
) {
}

