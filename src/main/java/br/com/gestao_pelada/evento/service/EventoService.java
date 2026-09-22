package br.com.gestao_pelada.evento.service;

import br.com.gestao_pelada.evento.model.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.model.dto.EventoResponseDTO;
import br.com.gestao_pelada.evento.model.mapper.EventoMapper;
import br.com.gestao_pelada.evento.model.entity.Evento;
import br.com.gestao_pelada.evento.repository.EventoRepository;
import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.jogador.repository.JogadorRepository;
import br.com.gestao_pelada.partida.repository.PartidaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;
    private final EventoMapper mapper;

    public EventoResponseDTO registrar(Long partidaId, EventoRequestDTO request) {
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
    public List<EventoResponseDTO> listarPorPartida(Long partidaId) {
        return eventoRepository.findByPartidaId(partidaId).stream()
                .map(evento -> toResponse(evento, jogadorRepository.findById(evento.getJogadorId()).orElse(null)))
                .toList();
    }

    public void remover(Long eventoId) {
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

