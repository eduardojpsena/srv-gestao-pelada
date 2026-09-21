package br.com.gestao_pelada.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "O refresh token deve ser informado")
        String refreshToken
) {
}
