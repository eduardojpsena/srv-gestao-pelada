package br.com.gestao_pelada.sorteio.service.strategy;

import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.sorteio.model.enums.TipoSorteio;

import java.util.ArrayList;
import java.util.List;

public interface SorteioStrategy {

    TipoSorteio getTipo();

    List<List<Jogador>> sortear(List<Jogador> jogadores, int numeroTimes);

    default List<List<Jogador>> criarTimesVazios(int numeroTimes) {
        List<List<Jogador>> times = new ArrayList<>(numeroTimes);
        for (int i = 0; i < numeroTimes; i++) {
            times.add(new ArrayList<>());
        }
        return times;
    }
}

