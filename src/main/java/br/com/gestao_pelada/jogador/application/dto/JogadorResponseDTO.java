package br.com.gestao_pelada.jogador.application.dto;

import br.com.gestao_pelada.jogador.domain.Posicao;

import java.util.UUID;

public record JogadorResponseDTO(
        UUID id,
        String nome,
        String apelido,
        String telefone,
        String email,
        Double notaGeral,
        Posicao posicao,
        boolean ativo
) {
}
