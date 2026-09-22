package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;

import java.time.Instant;

public record SolicitacaoEntradaResponseDTO(
        Long id,
        Long peladaId,
        Long usuarioId,
        String nome,
        String email,
        StatusSolicitacaoEntrada status,
        Instant criadoEm
) {
}

