package br.com.gestao_pelada.evento.application.dto;

import br.com.gestao_pelada.evento.domain.TipoEvento;

import java.util.UUID;

public record EventoResponseDTO(
        UUID id,
        UUID partidaId,
        UUID timeId,
        UUID jogadorId,
        String nomeJogador,
        TipoEvento tipo,
        Integer minuto
) {
}
