package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.PapelPelada;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MembroRequestDTO(
        @NotBlank(message = "O e-mail deve ser informado")
        @Email(message = "E-mail invalido")
        String email,
        PapelPelada papel
) {
}
