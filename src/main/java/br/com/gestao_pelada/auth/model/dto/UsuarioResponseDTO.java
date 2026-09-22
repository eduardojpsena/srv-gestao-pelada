package br.com.gestao_pelada.auth.model.dto;


public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        boolean ativo,
        boolean cadastroConcluido
) {
}

