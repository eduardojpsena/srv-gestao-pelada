package br.com.gestao_pelada.time.application.dto;

import java.util.UUID;

public record TimeJogadorResponseDTO(
        UUID jogadorId,
        String nomeJogador,
        Double notaGeral,
        boolean goleiro
) {
}
