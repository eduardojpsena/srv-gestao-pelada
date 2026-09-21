package br.com.gestao_pelada.auth.application.dto;

import br.com.gestao_pelada.shared.enums.Role;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        Role role,
        boolean ativo
) {
}
