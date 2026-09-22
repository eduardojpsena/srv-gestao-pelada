package br.com.gestao_pelada.time.model.dto;


public record TimeJogadorResponseDTO(
        Long jogadorId,
        String nomeJogador,
        Double notaGeral,
        boolean goleiro
) {
}

