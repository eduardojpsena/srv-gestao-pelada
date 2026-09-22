package br.com.gestao_pelada.sorteio.service.strategy;

import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.sorteio.model.enums.TipoSorteio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SorteioStrategiesTest {

    private final AvulsoSorteioStrategy avulsoSorteioStrategy = new AvulsoSorteioStrategy();
    private final EstrelasSorteioStrategy estrelasSorteioStrategy = new EstrelasSorteioStrategy();
    private final PoteSorteioStrategy poteSorteioStrategy = new PoteSorteioStrategy();

    private Jogador jogador(double nota) {
        return Jogador.builder()
                .id((long) (Math.random() * Long.MAX_VALUE))
                .nome("Jogador " + nota)
                .notaGeral(nota)
                .ativo(true)
                .build();
    }

    private List<Jogador> jogadoresComNotas(double... notas) {
        return java.util.stream.DoubleStream.of(notas)
                .mapToObj(this::jogador)
                .toList();
    }

    @Test
    void avulsoDeveDistribuirTodosOsJogadoresEmDoisTimes() {
        List<Jogador> jogadores = jogadoresComNotas(5, 4, 3, 2, 1, 1);

        List<List<Jogador>> times = avulsoSorteioStrategy.sortear(jogadores, 2);

        assertThat(times).hasSize(2);
        int total = times.stream().mapToInt(List::size).sum();
        assertThat(total).isEqualTo(jogadores.size());
        assertThat(times.get(0)).hasSizeBetween(2, 4);
        assertThat(times.get(1)).hasSizeBetween(2, 4);
    }

    @Test
    void avulsoNaoDeveRepetirOuPerderJogadores() {
        List<Jogador> jogadores = jogadoresComNotas(5, 4, 3, 2, 1, 1, 2, 3);

        List<List<Jogador>> times = avulsoSorteioStrategy.sortear(jogadores, 3);

        List<Jogador> todosAlocados = times.stream().flatMap(List::stream).toList();
        assertThat(todosAlocados).hasSameSizeAs(jogadores);
        assertThat(todosAlocados).containsExactlyInAnyOrderElementsOf(jogadores);
    }

    @Test
    void estrelasDeveEquilibrarSomaDeNotasEntreOsTimes() {
        List<Jogador> jogadores = jogadoresComNotas(5, 5, 1, 1, 3, 3);

        List<List<Jogador>> times = estrelasSorteioStrategy.sortear(jogadores, 2);

        assertThat(times).hasSize(2);
        double somaTime1 = times.get(0).stream().mapToDouble(Jogador::getNotaGeral).sum();
        double somaTime2 = times.get(1).stream().mapToDouble(Jogador::getNotaGeral).sum();
        assertThat(somaTime1).isEqualTo(somaTime2);
    }

    @Test
    void estrelasDeveManterTodosOsJogadoresDistribuidos() {
        List<Jogador> jogadores = jogadoresComNotas(5, 4, 3, 2, 1);

        List<List<Jogador>> times = estrelasSorteioStrategy.sortear(jogadores, 2);

        List<Jogador> todosAlocados = times.stream().flatMap(List::stream).toList();
        assertThat(todosAlocados).hasSameSizeAs(jogadores);
    }

    @Test
    void potesDeveDistribuirUmJogadorPorTimeACadaPote() {
        List<Jogador> jogadores = jogadoresComNotas(5, 4, 3, 2, 1, 0.5);

        List<List<Jogador>> times = poteSorteioStrategy.sortear(jogadores, 3);

        assertThat(times).hasSize(3);
        List<Jogador> todosAlocados = times.stream().flatMap(List::stream).toList();
        assertThat(todosAlocados).hasSameSizeAs(jogadores);
        times.forEach(time -> assertThat(time).hasSize(2));
    }

    @Test
    void deveLancarExcecaoDeIndiceForaDosLimitesQuandoNaoHaJogadoresSuficientes() {
        List<Jogador> jogadores = jogadoresComNotas(5, 4);

        List<List<Jogador>> times = avulsoSorteioStrategy.sortear(jogadores, 2);

        assertThat(times).hasSize(2);
        assertThat(times.get(0)).hasSize(1);
        assertThat(times.get(1)).hasSize(1);
    }
}
