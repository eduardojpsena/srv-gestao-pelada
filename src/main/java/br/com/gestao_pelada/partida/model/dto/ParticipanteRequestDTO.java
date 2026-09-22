package br.com.gestao_pelada.partida.model.dto;

import jakarta.validation.constraints.NotNull;


public record ParticipanteRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        Long jogadorId,
        Boolean goleiro
) {
}

