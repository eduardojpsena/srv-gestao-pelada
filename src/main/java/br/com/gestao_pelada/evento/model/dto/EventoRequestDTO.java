package br.com.gestao_pelada.evento.model.dto;

import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.validation.constraints.NotNull;


public record EventoRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        Long jogadorId,
        Long timeId,
        @NotNull(message = "O tipo de evento deve ser informado")
        TipoEvento tipo,
        Integer minuto
) {
}

