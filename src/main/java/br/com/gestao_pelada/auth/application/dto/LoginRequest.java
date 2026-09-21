package br.com.gestao_pelada.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O e-mail deve ser informado")
        @Email(message = "E-mail invalido")
        String email,
        @NotBlank(message = "A senha deve ser informada")
        String senha
) {
}
