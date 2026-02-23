package br.com.gestao_pelada.adapter.inbound.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record JogadorRequestDTO(
        @NotEmpty(message = "O nome deve ser informado")
        String nome,
        String apelido,
        String telefone,
        String email,
        @NotNull(message = "A nota deve ser informada")
        Double nota,
        String posicao
) {
}
