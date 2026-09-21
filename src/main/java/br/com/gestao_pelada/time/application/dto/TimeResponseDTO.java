package br.com.gestao_pelada.time.application.dto;

import java.util.List;
import java.util.UUID;

public record TimeResponseDTO(
        UUID id,
        UUID partidaId,
        String nome,
        String cor,
        Double notaTotal,
        List<TimeJogadorResponseDTO> jogadores
) {
}
