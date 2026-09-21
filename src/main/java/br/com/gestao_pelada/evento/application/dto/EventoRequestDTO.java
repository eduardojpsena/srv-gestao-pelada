package br.com.gestao_pelada.evento.application.dto;

import br.com.gestao_pelada.evento.domain.TipoEvento;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EventoRequestDTO(
        @NotNull(message = "O jogador deve ser informado")
        UUID jogadorId,
        UUID timeId,
        @NotNull(message = "O tipo de evento deve ser informado")
        TipoEvento tipo,
        Integer minuto
) {
}
