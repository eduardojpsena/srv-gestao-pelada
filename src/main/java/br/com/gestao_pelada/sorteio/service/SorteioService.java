package br.com.gestao_pelada.sorteio.service;

import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.jogador.repository.JogadorRepository;
import br.com.gestao_pelada.partida.model.entity.Participante;
import br.com.gestao_pelada.partida.model.entity.Partida;
import br.com.gestao_pelada.partida.repository.ParticipanteRepository;
import br.com.gestao_pelada.partida.repository.PartidaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import br.com.gestao_pelada.sorteio.model.dto.SorteioRequestDTO;
import br.com.gestao_pelada.sorteio.service.strategy.SorteioStrategy;
import br.com.gestao_pelada.sorteio.model.enums.TipoSorteio;
import br.com.gestao_pelada.time.service.TimeService;
import br.com.gestao_pelada.time.model.dto.TimeResponseDTO;
import br.com.gestao_pelada.time.model.entity.Time;
import br.com.gestao_pelada.time.model.entity.TimeJogador;
import br.com.gestao_pelada.time.repository.TimeJogadorRepository;
import br.com.gestao_pelada.time.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    public List<TimeResponseDTO> sortear(Long partidaId, SorteioRequestDTO request) {
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

        Map<Long, Jogador> jogadoresPorId = jogadorRepository
                .findByIdIn(participantes.stream().map(Participante::getJogadorId).toList())
                .stream()
                .collect(Collectors.toMap(Jogador::getId, Function.identity()));

        Map<Long, Boolean> goleirosPorJogador = participantes.stream()
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

