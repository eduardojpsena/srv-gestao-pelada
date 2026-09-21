package br.com.gestao_pelada.evento.application;

import br.com.gestao_pelada.evento.application.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.application.dto.EventoResponseDTO;
import br.com.gestao_pelada.evento.application.mapper.EventoMapper;
import br.com.gestao_pelada.evento.domain.Evento;
import br.com.gestao_pelada.evento.infrastructure.EventoRepository;
import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.partida.infrastructure.PartidaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;
    private final EventoMapper mapper;

    public EventoResponseDTO registrar(UUID partidaId, EventoRequestDTO request) {
        if (!partidaRepository.existsById(partidaId)) {
            throw new ResourceNotFoundException("Partida nao encontrada: " + partidaId);
        }
        Jogador jogador = jogadorRepository.findById(request.jogadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + request.jogadorId()));

        if (request.minuto() != null && request.minuto() < 0) {
            throw new BusinessException("O minuto do evento nao pode ser negativo");
        }

        Evento evento = mapper.toEntity(request);
        evento.setPartidaId(partidaId);
        Evento salvo = eventoRepository.save(evento);
        return toResponse(salvo, jogador);
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarPorPartida(UUID partidaId) {
        return eventoRepository.findByPartidaId(partidaId).stream()
                .map(evento -> toResponse(evento, jogadorRepository.findById(evento.getJogadorId()).orElse(null)))
                .toList();
    }

    public void remover(UUID eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento nao encontrado: " + eventoId);
        }
        eventoRepository.deleteById(eventoId);
    }

    private EventoResponseDTO toResponse(Evento evento, Jogador jogador) {
        return new EventoResponseDTO(
                evento.getId(),
                evento.getPartidaId(),
                evento.getTimeId(),
                evento.getJogadorId(),
                jogador != null ? jogador.getNome() : null,
                evento.getTipo(),
                evento.getMinuto()
        );
    }
}
