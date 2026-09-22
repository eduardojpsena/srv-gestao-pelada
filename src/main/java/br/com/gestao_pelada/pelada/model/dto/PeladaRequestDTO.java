package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record PeladaRequestDTO(
        @NotBlank(message = "O nome deve ser informado")
        String nome,
        String descricao,
        DiaSemana diaSemana,
        LocalTime horario,
        String local
) {
}

