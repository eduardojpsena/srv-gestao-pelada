package br.com.gestao_pelada.pelada.model.dto;

import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.validation.constraints.NotNull;

public record DecisaoSolicitacaoRequestDTO(
        @NotNull(message = "A decisao deve ser informada")
        StatusSolicitacaoEntrada status
) {
}

