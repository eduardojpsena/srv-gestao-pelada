package br.com.gestao_pelada.evento.model.dto;

import br.com.gestao_pelada.evento.model.enums.TipoEvento;


public record EventoResponseDTO(
        Long id,
        Long partidaId,
        Long timeId,
        Long jogadorId,
        String nomeJogador,
        TipoEvento tipo,
        Integer minuto
) {
}

