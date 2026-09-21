package br.com.gestao_pelada.model.dto;

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
