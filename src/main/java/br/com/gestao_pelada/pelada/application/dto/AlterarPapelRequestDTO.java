package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.PapelPelada;
import jakarta.validation.constraints.NotNull;

public record AlterarPapelRequestDTO(
        @NotNull(message = "O papel deve ser informado")
        PapelPelada papel
) {
}
