package br.com.gestao_pelada.partida.model.dto;

import br.com.gestao_pelada.partida.model.enums.StatusPartida;

import java.time.LocalDate;
import java.time.LocalTime;

public record PartidaResponseDTO(
        Long id,
        Long peladaId,
        LocalDate data,
        LocalTime horario,
        String local,
        StatusPartida status,
        Integer numeroTimes,
        Integer jogadoresPorTime
) {
}

