package br.com.gestao_pelada.adapter.inbound.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record JogadorRequestDTO(
        @NotEmpty(message = "O nome deve ser informado") String nome,
        String apelido,
        @NotNull(message = "A quantidade de estrelas deve ser informda") Double estrela,
        String posicao
) {
}
