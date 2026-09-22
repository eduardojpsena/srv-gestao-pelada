package br.com.gestao_pelada.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome deve ser informado")
        String nome,
        @NotBlank(message = "O e-mail deve ser informado")
        @Email(message = "E-mail invalido")
        String email,
        @NotBlank(message = "A senha deve ser informada")
        @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres")
        String senha
) {
}

