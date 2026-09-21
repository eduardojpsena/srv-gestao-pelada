package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record PeladaRequestDTO(
        @NotBlank(message = "O nome deve ser informado")
        String nome,
        String descricao,
        DiaSemana diaSemana,
        LocalTime horario,
        String local,
        @NotNull(message = "O organizador deve ser informado")
        java.util.UUID organizadorId
) {
}
