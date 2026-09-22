package br.com.gestao_pelada.auth.application.dto;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        boolean ativo,
        boolean cadastroConcluido
) {
}
