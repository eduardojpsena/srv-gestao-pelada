package br.com.gestao_pelada.domain.model;

public record Jogador(
        Long id,
        String nome,
        String apelido,
        Double estrela,
        String posicao
) {
}
