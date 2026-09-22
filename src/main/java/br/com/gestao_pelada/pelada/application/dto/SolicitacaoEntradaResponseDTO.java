package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.StatusSolicitacaoEntrada;

import java.time.Instant;
import java.util.UUID;

public record SolicitacaoEntradaResponseDTO(
        UUID id,
        UUID peladaId,
        UUID usuarioId,
        String nome,
        String email,
        StatusSolicitacaoEntrada status,
        Instant criadoEm
) {
}
