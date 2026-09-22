package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;

import java.time.LocalTime;

public record PeladaResponseDTO(
        Long id,
        String nome,
        String descricao,
        DiaSemana diaSemana,
        LocalTime horario,
        String local,
        Long organizadorId,
        StatusPelada status
) {
}

