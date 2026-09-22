package br.com.gestao_pelada.time.model.dto;

import java.util.List;

public record TimeResponseDTO(
        Long id,
        Long partidaId,
        String nome,
        String cor,
        Double notaTotal,
        List<TimeJogadorResponseDTO> jogadores
) {
}

