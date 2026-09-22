package br.com.gestao_pelada.pelada.model.dto;

import jakarta.validation.constraints.NotNull;


public record PeladaJogadorRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        Long jogadorId,
        Double notaPelada,
        Boolean mensalista
) {
}

