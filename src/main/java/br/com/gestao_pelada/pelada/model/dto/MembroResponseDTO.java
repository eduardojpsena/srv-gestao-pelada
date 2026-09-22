package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.PapelPelada;


public record MembroResponseDTO(
        Long id,
        Long usuarioId,
        String nome,
        String email,
        PapelPelada papel,
        boolean cadastroConcluido,
        boolean ativo
) {
}

