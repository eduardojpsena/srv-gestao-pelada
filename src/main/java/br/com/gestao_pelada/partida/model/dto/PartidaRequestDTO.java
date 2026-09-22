package br.com.gestao_pelada.partida.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalTime;

public record PartidaRequestDTO(
        @NotNull(message = "A data deve ser informada")
        LocalDate data,
        LocalTime horario,
        String local,
        @Min(value = 2, message = "Devem existir ao menos 2 times")
        Integer numeroTimes,
        Integer jogadoresPorTime
) {
}

