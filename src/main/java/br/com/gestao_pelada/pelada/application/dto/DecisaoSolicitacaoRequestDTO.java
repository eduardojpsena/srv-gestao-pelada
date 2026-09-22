package br.com.gestao_pelada.pelada.application.dto;

import br.com.gestao_pelada.pelada.domain.StatusSolicitacaoEntrada;
import jakarta.validation.constraints.NotNull;

public record DecisaoSolicitacaoRequestDTO(
        @NotNull(message = "A decisao deve ser informada")
        StatusSolicitacaoEntrada status
) {
}
