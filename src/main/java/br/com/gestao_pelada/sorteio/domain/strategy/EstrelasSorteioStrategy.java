package br.com.gestao_pelada.sorteio.domain.strategy;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.sorteio.domain.SorteioStrategy;
import br.com.gestao_pelada.sorteio.domain.TipoSorteio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Balanceamento por estrelas (nota): ordena os jogadores da maior para a menor nota
 * e, de forma gulosa, aloca cada jogador ao time com a menor soma de notas ate o momento,
 * buscando o maior equilibrio possivel entre os times.
 */
@Component
public class EstrelasSorteioStrategy implements SorteioStrategy {

    @Override
    public TipoSorteio getTipo() {
        return TipoSorteio.ESTRELAS;
    }

    @Override
    public List<List<Jogador>> sortear(List<Jogador> jogadores, int numeroTimes) {
        List<Jogador> ordenados = new ArrayList<>(jogadores);
        ordenados.sort(Comparator.comparing(Jogador::getNotaGeral, Comparator.nullsLast(Comparator.reverseOrder())));

        List<List<Jogador>> times = criarTimesVazios(numeroTimes);
        double[] somaPorTime = new double[numeroTimes];

        for (Jogador jogador : ordenados) {
            int indiceMenorTime = indiceDoMenor(somaPorTime);
            times.get(indiceMenorTime).add(jogador);
            somaPorTime[indiceMenorTime] += jogador.getNotaGeral() != null ? jogador.getNotaGeral() : 0.0;
        }

        return times;
    }

    private int indiceDoMenor(double[] valores) {
        int indiceMenor = 0;
        for (int i = 1; i < valores.length; i++) {
            if (valores[i] < valores[indiceMenor]) {
                indiceMenor = i;
            }
        }
        return indiceMenor;
    }
}
