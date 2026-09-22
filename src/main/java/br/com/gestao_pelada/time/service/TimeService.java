package br.com.gestao_pelada.time.service;

import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.jogador.repository.JogadorRepository;
import br.com.gestao_pelada.time.model.dto.TimeJogadorResponseDTO;
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

@Service
@RequiredArgsConstructor
@Transactional
public class TimeService {

    private final TimeRepository timeRepository;
    private final TimeJogadorRepository timeJogadorRepository;
    private final JogadorRepository jogadorRepository;

    @Transactional(readOnly = true)
    public List<TimeResponseDTO> listarPorPartida(Long partidaId) {
        List<Time> times = timeRepository.findByPartidaId(partidaId);
        if (times.isEmpty()) {
            return List.of();
        }

        List<Long> timeIds = times.stream().map(Time::getId).toList();
        List<TimeJogador> vinculos = timeJogadorRepository.findByTimeIdIn(timeIds);

        Map<Long, Jogador> jogadoresPorId = jogadorRepository
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

    public void removerTimesDaPartida(Long partidaId) {
        List<Time> times = timeRepository.findByPartidaId(partidaId);
        if (times.isEmpty()) {
            return;
        }
        List<Long> timeIds = times.stream().map(Time::getId).toList();
        timeJogadorRepository.deleteByTimeIdIn(timeIds);
        timeRepository.deleteByPartidaId(partidaId);
    }
}

