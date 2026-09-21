package br.com.gestao_pelada.sorteio.application.dto;

import br.com.gestao_pelada.sorteio.domain.TipoSorteio;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SorteioRequestDTO(
        @NotNull(message = "O tipo de sorteio deve ser informado")
        TipoSorteio tipo,
        @Min(value = 2, message = "Devem existir ao menos 2 times")
        Integer numeroTimes
) {
}
