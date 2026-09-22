package br.com.gestao_pelada.auth.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank(message = "O refresh token deve ser informado")
        String refreshToken
) {
}

