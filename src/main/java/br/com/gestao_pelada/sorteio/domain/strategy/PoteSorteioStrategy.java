package br.com.gestao_pelada.sorteio.domain.strategy;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.sorteio.domain.SorteioStrategy;
import br.com.gestao_pelada.sorteio.domain.TipoSorteio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Divide os jogadores em "potes" (faixas de nota), do mais forte para o mais fraco.
 * Cada pote e embaralhado e distribuido um jogador por time, alternando o sentido
 * (snake draft) para manter o equilibrio geral entre os times.
 */
@Component
public class PoteSorteioStrategy implements SorteioStrategy {

    @Override
    public TipoSorteio getTipo() {
        return TipoSorteio.POTES;
    }

    @Override
    public List<List<Jogador>> sortear(List<Jogador> jogadores, int numeroTimes) {
        List<Jogador> ordenados = new ArrayList<>(jogadores);
        ordenados.sort(Comparator.comparing(Jogador::getNotaGeral, Comparator.nullsLast(Comparator.reverseOrder())));

        List<List<Jogador>> times = criarTimesVazios(numeroTimes);

        int indice = 0;
        int numeroPote = 0;
        while (indice < ordenados.size()) {
            int fim = Math.min(indice + numeroTimes, ordenados.size());
            List<Jogador> pote = new ArrayList<>(ordenados.subList(indice, fim));
            Collections.shuffle(pote);
            if (numeroPote % 2 == 1) {
                Collections.reverse(pote);
            }
            for (int i = 0; i < pote.size(); i++) {
                times.get(i).add(pote.get(i));
            }
            indice = fim;
            numeroPote++;
        }
        return times;
    }
}
