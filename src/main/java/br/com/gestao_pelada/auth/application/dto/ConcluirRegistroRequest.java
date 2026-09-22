package br.com.gestao_pelada.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConcluirRegistroRequest(
        @NotBlank(message = "O e-mail deve ser informado")
        @Email(message = "E-mail invalido")
        String email,
        @NotBlank(message = "A senha inicial deve ser informada")
        String senhaInicial,
        @NotBlank(message = "A nova senha deve ser informada")
        @Size(min = 6, message = "A nova senha deve ter no minimo 6 caracteres")
        String novaSenha
) {
}
