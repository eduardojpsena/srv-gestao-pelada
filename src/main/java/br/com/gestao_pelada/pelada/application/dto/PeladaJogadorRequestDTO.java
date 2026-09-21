package br.com.gestao_pelada.pelada.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PeladaJogadorRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        UUID jogadorId,
        Double notaPelada,
        Boolean mensalista
) {
}
