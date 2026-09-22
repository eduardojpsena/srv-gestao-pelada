package br.com.gestao_pelada.pelada.model.dto;


public record PeladaJogadorResponseDTO(
        Long id,
        Long peladaId,
        Long jogadorId,
        String nomeJogador,
        Double notaPelada,
        boolean mensalista,
        boolean ativo
) {
}

