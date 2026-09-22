package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.PapelPelada;

import java.util.UUID;

public record MembroResponseDTO(
        UUID id,
        UUID usuarioId,
        String nome,
        String email,
        PapelPelada papel,
        boolean cadastroConcluido,
        boolean ativo
) {
}
