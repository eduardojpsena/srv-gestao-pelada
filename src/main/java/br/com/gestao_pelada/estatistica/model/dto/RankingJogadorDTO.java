package br.com.gestao_pelada.estatistica.model.dto;


public record RankingJogadorDTO(
        Long jogadorId,
        String nomeJogador,
        long partidasJogadas,
        long gols,
        long assistencias,
        long cartoesAmarelos,
        long cartoesVermelhos
) {
}

