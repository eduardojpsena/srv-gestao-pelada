package br.com.gestao_pelada.jogador.model.dto;

import br.com.gestao_pelada.jogador.model.enums.Posicao;


public record JogadorResponseDTO(
        Long id,
        String nome,
        String apelido,
        String telefone,
        String email,
        Double notaGeral,
        Posicao posicao,
        boolean ativo
) {
}

