package br.com.gestao_pelada.partida.application.dto;

import java.util.UUID;

public record ParticipanteResponseDTO(
        UUID id,
        UUID partidaId,
        UUID jogadorId,
        String nomeJogador,
        boolean confirmado,
        boolean presente,
        boolean goleiro
) {
}
