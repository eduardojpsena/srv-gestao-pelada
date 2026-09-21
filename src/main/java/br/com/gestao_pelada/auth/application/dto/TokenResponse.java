package br.com.gestao_pelada.auth.application.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMs
) {
}
