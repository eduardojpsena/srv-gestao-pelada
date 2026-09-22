package br.com.gestao_pelada.partida.model.dto;


public record ParticipanteResponseDTO(
        Long id,
        Long partidaId,
        Long jogadorId,
        String nomeJogador,
        boolean confirmado,
        boolean presente,
        boolean goleiro
) {
}

