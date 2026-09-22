package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import jakarta.validation.constraints.NotNull;

public record AlterarPapelRequestDTO(
        @NotNull(message = "O papel deve ser informado")
        PapelPelada papel
) {
}

