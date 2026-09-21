package br.com.gestao_pelada.sorteio.application;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.partida.domain.Participante;
import br.com.gestao_pelada.partida.domain.Partida;
import br.com.gestao_pelada.partida.infrastructure.ParticipanteRepository;
import br.com.gestao_pelada.partida.infrastructure.PartidaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import br.com.gestao_pelada.sorteio.application.dto.SorteioRequestDTO;
import br.com.gestao_pelada.sorteio.domain.SorteioStrategy;
import br.com.gestao_pelada.sorteio.domain.TipoSorteio;
import br.com.gestao_pelada.time.application.TimeService;
import br.com.gestao_pelada.time.application.dto.TimeResponseDTO;
import br.com.gestao_pelada.time.domain.Time;
import br.com.gestao_pelada.time.domain.TimeJogador;
import br.com.gestao_pelada.time.infrastructure.TimeJogadorRepository;
import br.com.gestao_pelada.time.infrastructure.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SorteioService {

    private final PartidaRepository partidaRepository;
    private final ParticipanteRepository participanteRepository;
    private final JogadorRepository jogadorRepository;
    private final TimeRepository timeRepository;
    private final TimeJogadorRepository timeJogadorRepository;
    private final TimeService timeService;
    private final List<SorteioStrategy> estrategias;

    private Map<TipoSorteio, SorteioStrategy> estrategiasPorTipo;

    private Map<TipoSorteio, SorteioStrategy> estrategias() {
        if (estrategiasPorTipo == null) {
            estrategiasPorTipo = estrategias.stream()
                    .collect(Collectors.toMap(SorteioStrategy::getTipo, Function.identity()));
        }
        return estrategiasPorTipo;
    }

    public List<TimeResponseDTO> sortear(UUID partidaId, SorteioRequestDTO request) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida nao encontrada: " + partidaId));

        int numeroTimes = request.numeroTimes() != null ? request.numeroTimes() : partida.getNumeroTimes();
        if (numeroTimes < 2) {
            throw new BusinessException("O numero de times deve ser no minimo 2");
        }

        List<Participante> participantes = participanteRepository.findByPartidaIdAndConfirmadoTrue(partidaId);
        if (participantes.size() < numeroTimes) {
            throw new BusinessException("Nao ha jogadores confirmados suficientes para formar " + numeroTimes + " times");
        }

        Map<UUID, Jogador> jogadoresPorId = jogadorRepository
                .findByIdIn(participantes.stream().map(Participante::getJogadorId).toList())
                .stream()
                .collect(Collectors.toMap(Jogador::getId, Function.identity()));

        Map<UUID, Boolean> goleirosPorJogador = participantes.stream()
                .collect(Collectors.toMap(Participante::getJogadorId, Participante::isGoleiro));

        List<Jogador> jogadores = participantes.stream()
                .map(p -> jogadoresPorId.get(p.getJogadorId()))
                .filter(java.util.Objects::nonNull)
                .toList();

        SorteioStrategy estrategia = estrategias().get(request.tipo());
        if (estrategia == null) {
            throw new BusinessException("Tipo de sorteio nao suportado: " + request.tipo());
        }

        timeService.removerTimesDaPartida(partidaId);

        List<List<Jogador>> timesSorteados = estrategia.sortear(jogadores, numeroTimes);

        for (int i = 0; i < timesSorteados.size(); i++) {
            List<Jogador> jogadoresDoTime = timesSorteados.get(i);
            double notaTotal = jogadoresDoTime.stream()
                    .mapToDouble(j -> j.getNotaGeral() != null ? j.getNotaGeral() : 0.0)
                    .sum();

            Time time = timeRepository.save(Time.builder()
                    .partidaId(partidaId)
                    .nome("Time " + (i + 1))
                    .notaTotal(notaTotal)
                    .build());

            for (Jogador jogador : jogadoresDoTime) {
                timeJogadorRepository.save(TimeJogador.builder()
                        .timeId(time.getId())
                        .jogadorId(jogador.getId())
                        .goleiro(Boolean.TRUE.equals(goleirosPorJogador.get(jogador.getId())))
                        .build());
            }
        }

        return timeService.listarPorPartida(partidaId);
    }
}
