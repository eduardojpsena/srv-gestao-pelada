package br.com.gestao_pelada.estatistica.application.dto;

import java.util.UUID;

public record RankingJogadorDTO(
        UUID jogadorId,
        String nomeJogador,
        long partidasJogadas,
        long gols,
        long assistencias,
        long cartoesAmarelos,
        long cartoesVermelhos
) {
}
