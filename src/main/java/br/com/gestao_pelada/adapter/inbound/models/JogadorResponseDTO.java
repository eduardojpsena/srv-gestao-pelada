package br.com.gestao_pelada.adapter.inbound.models;

public record JogadorResponseDTO(
        Long id,
        String nome,
        String apelido,
        String telefone,
        String email,
        Double nota,
        String posicao
) {
}
