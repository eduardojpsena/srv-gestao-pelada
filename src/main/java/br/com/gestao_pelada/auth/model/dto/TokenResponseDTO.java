package br.com.gestao_pelada.auth.model.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMs
) {
}

