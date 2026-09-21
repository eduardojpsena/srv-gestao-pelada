package br.com.gestao_pelada.time.application;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.time.application.dto.TimeJogadorResponseDTO;
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

@Service
@RequiredArgsConstructor
@Transactional
public class TimeService {

    private final TimeRepository timeRepository;
    private final TimeJogadorRepository timeJogadorRepository;
    private final JogadorRepository jogadorRepository;

    @Transactional(readOnly = true)
    public List<TimeResponseDTO> listarPorPartida(UUID partidaId) {
        List<Time> times = timeRepository.findByPartidaId(partidaId);
        if (times.isEmpty()) {
            return List.of();
        }

        List<UUID> timeIds = times.stream().map(Time::getId).toList();
        List<TimeJogador> vinculos = timeJogadorRepository.findByTimeIdIn(timeIds);

        Map<UUID, Jogador> jogadoresPorId = jogadorRepository
                .findByIdIn(vinculos.stream().map(TimeJogador::getJogadorId).toList())
                .stream()
                .collect(java.util.stream.Collectors.toMap(Jogador::getId, j -> j));

        return times.stream()
                .map(time -> {
                    List<TimeJogadorResponseDTO> jogadoresDoTime = vinculos.stream()
                            .filter(v -> v.getTimeId().equals(time.getId()))
                            .map(v -> {
                                Jogador jogador = jogadoresPorId.get(v.getJogadorId());
                                return new TimeJogadorResponseDTO(
                                        v.getJogadorId(),
                                        jogador != null ? jogador.getNome() : null,
                                        jogador != null ? jogador.getNotaGeral() : null,
                                        v.isGoleiro());
                            })
                            .toList();
                    return new TimeResponseDTO(time.getId(), time.getPartidaId(), time.getNome(), time.getCor(), time.getNotaTotal(), jogadoresDoTime);
                })
                .toList();
    }

    public void removerTimesDaPartida(UUID partidaId) {
        List<Time> times = timeRepository.findByPartidaId(partidaId);
        if (times.isEmpty()) {
            return;
        }
        List<UUID> timeIds = times.stream().map(Time::getId).toList();
        timeJogadorRepository.deleteByTimeIdIn(timeIds);
        timeRepository.deleteByPartidaId(partidaId);
    }
}
