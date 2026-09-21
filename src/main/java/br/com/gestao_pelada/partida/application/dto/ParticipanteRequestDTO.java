package br.com.gestao_pelada.partida.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipanteRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        UUID jogadorId,
        Boolean goleiro
) {
}
