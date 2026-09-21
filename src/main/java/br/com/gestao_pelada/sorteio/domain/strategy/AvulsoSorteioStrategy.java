package br.com.gestao_pelada.sorteio.domain.strategy;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.sorteio.domain.SorteioStrategy;
import br.com.gestao_pelada.sorteio.domain.TipoSorteio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sorteio avulso: totalmente aleatorio, sem considerar a nota dos jogadores.
 * Util para peladas casuais em que o equilibrio tecnico nao e prioridade.
 */
@Component
public class AvulsoSorteioStrategy implements SorteioStrategy {

    @Override
    public TipoSorteio getTipo() {
        return TipoSorteio.AVULSO;
    }

    @Override
    public List<List<Jogador>> sortear(List<Jogador> jogadores, int numeroTimes) {
        List<Jogador> embaralhados = new ArrayList<>(jogadores);
        Collections.shuffle(embaralhados);

        List<List<Jogador>> times = criarTimesVazios(numeroTimes);
        for (int i = 0; i < embaralhados.size(); i++) {
            times.get(i % numeroTimes).add(embaralhados.get(i));
        }
        return times;
    }
}
