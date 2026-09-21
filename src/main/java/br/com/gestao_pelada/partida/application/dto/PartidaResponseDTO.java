package br.com.gestao_pelada.partida.application.dto;

import br.com.gestao_pelada.partida.domain.StatusPartida;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record PartidaResponseDTO(
        UUID id,
        UUID peladaId,
        LocalDate data,
        LocalTime horario,
        String local,
        StatusPartida status,
        Integer numeroTimes,
        Integer jogadoresPorTime
) {
}
