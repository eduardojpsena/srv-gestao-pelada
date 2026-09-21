package br.com.gestao_pelada.pelada.application.dto;

import java.util.UUID;

public record PeladaJogadorResponseDTO(
        UUID id,
        UUID peladaId,
        UUID jogadorId,
        String nomeJogador,
        Double notaPelada,
        boolean mensalista,
        boolean ativo
) {
}
