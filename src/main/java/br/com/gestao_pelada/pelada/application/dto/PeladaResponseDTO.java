package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.DiaSemana;
import br.com.gestao_pelada.pelada.domain.StatusPelada;

import java.time.LocalTime;
import java.util.UUID;

public record PeladaResponseDTO(
        UUID id,
        String nome,
        String descricao,
        DiaSemana diaSemana,
        LocalTime horario,
        String local,
        UUID organizadorId,
        StatusPelada status
) {
}
