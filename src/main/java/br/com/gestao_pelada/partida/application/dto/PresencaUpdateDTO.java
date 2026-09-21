package br.com.gestao_pelada.partida.application.dto;

import jakarta.validation.constraints.NotNull;

public record PresencaUpdateDTO(
        @NotNull(message = "A presenca deve ser informada")
        Boolean presente
) {
}
