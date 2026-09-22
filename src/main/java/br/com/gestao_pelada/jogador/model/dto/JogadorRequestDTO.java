package br.com.gestao_pelada.jogador.model.dto;

import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record JogadorRequestDTO(
        @NotBlank(message = "O nome deve ser informado")
        String nome,
        String apelido,
        String telefone,
        @Email(message = "E-mail invalido")
        String email,
        @DecimalMin(value = "0.0", message = "A nota minima e 0.0")
        @DecimalMax(value = "5.0", message = "A nota maxima e 5.0")
        Double notaGeral,
        Posicao posicao
) {
}

