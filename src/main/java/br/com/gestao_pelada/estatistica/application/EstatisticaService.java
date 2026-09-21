package br.com.gestao_pelada.estatistica.application;

import br.com.gestao_pelada.estatistica.application.dto.RankingJogadorDTO;
import br.com.gestao_pelada.evento.domain.Evento;
import br.com.gestao_pelada.evento.domain.TipoEvento;
import br.com.gestao_pelada.evento.infrastructure.EventoRepository;
import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.partida.domain.Participante;
import br.com.gestao_pelada.partida.domain.Partida;
import br.com.gestao_pelada.partida.infrastructure.ParticipanteRepository;
import br.com.gestao_pelada.partida.infrastructure.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstatisticaService {

    private final PartidaRepository partidaRepository;
    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;
    private final JogadorRepository jogadorRepository;

    public List<RankingJogadorDTO> rankingCompleto(UUID peladaId) {
        List<UUID> partidaIds = partidaRepository.findAllByPeladaId(peladaId).stream()
                .map(Partida::getId)
                .toList();

        if (partidaIds.isEmpty()) {
            return List.of();
        }

        Map<UUID, long[]> contadores = new HashMap<>();
        for (Evento evento : eventoRepository.findByPartidaIdIn(partidaIds)) {
            long[] contador = contadores.computeIfAbsent(evento.getJogadorId(), id -> new long[4]);
            switch (evento.getTipo()) {
                case GOL -> contador[0]++;
                case ASSISTENCIA -> contador[1]++;
                case CARTAO_AMARELO -> contador[2]++;
                case CARTAO_VERMELHO -> contador[3]++;
                default -> throw new IllegalStateException("Tipo de evento desconhecido: " + evento.getTipo());
            }
        }

        Map<UUID, Long> partidasJogadasPorJogador = new HashMap<>();
        for (Participante participante : participanteRepository.findByPartidaIdIn(partidaIds)) {
            if (participante.isPresente()) {
                partidasJogadasPorJogador.merge(participante.getJogadorId(), 1L, Long::sum);
            }
        }

        java.util.Set<UUID> jogadorIds = new java.util.HashSet<>();
        jogadorIds.addAll(contadores.keySet());
        jogadorIds.addAll(partidasJogadasPorJogador.keySet());

        Map<UUID, Jogador> jogadoresPorId = jogadorRepository.findByIdIn(jogadorIds.stream().toList()).stream()
                .collect(java.util.stream.Collectors.toMap(Jogador::getId, j -> j));

        return jogadorIds.stream()
                .map(jogadorId -> {
                    long[] contador = contadores.getOrDefault(jogadorId, new long[4]);
                    Jogador jogador = jogadoresPorId.get(jogadorId);
                    return new RankingJogadorDTO(
                            jogadorId,
                            jogador != null ? jogador.getNome() : null,
                            partidasJogadasPorJogador.getOrDefault(jogadorId, 0L),
                            contador[0],
                            contador[1],
                            contador[2],
                            contador[3]
                    );
                })
                .sorted(Comparator.comparingLong(RankingJogadorDTO::gols).reversed()
                        .thenComparing(Comparator.comparingLong(RankingJogadorDTO::assistencias).reversed()))
                .toList();
    }

    public List<RankingJogadorDTO> artilheiros(UUID peladaId) {
        return rankingCompleto(peladaId).stream()
                .filter(r -> r.gols() > 0)
                .sorted(Comparator.comparingLong(RankingJogadorDTO::gols).reversed())
                .toList();
    }

    public List<RankingJogadorDTO> assistencias(UUID peladaId) {
        return rankingCompleto(peladaId).stream()
                .filter(r -> r.assistencias() > 0)
                .sorted(Comparator.comparingLong(RankingJogadorDTO::assistencias).reversed())
                .toList();
    }

    public List<RankingJogadorDTO> cartoes(UUID peladaId) {
        return rankingCompleto(peladaId).stream()
                .filter(r -> r.cartoesAmarelos() > 0 || r.cartoesVermelhos() > 0)
                .sorted(Comparator.comparingLong((RankingJogadorDTO r) -> r.cartoesAmarelos() + r.cartoesVermelhos()).reversed())
                .toList();
    }
}
