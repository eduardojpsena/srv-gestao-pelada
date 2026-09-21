package br.com.gestao_pelada.partida.application;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.partida.application.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.application.dto.ParticipanteRequestDTO;
import br.com.gestao_pelada.partida.application.dto.ParticipanteResponseDTO;
import br.com.gestao_pelada.partida.application.mapper.PartidaMapper;
import br.com.gestao_pelada.partida.domain.Participante;
import br.com.gestao_pelada.partida.domain.Partida;
import br.com.gestao_pelada.partida.domain.StatusPartida;
import br.com.gestao_pelada.partida.infrastructure.ParticipanteRepository;
import br.com.gestao_pelada.partida.infrastructure.PartidaRepository;
import br.com.gestao_pelada.pelada.infrastructure.PeladaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ParticipanteRepository participanteRepository;
    private final PeladaRepository peladaRepository;
    private final JogadorRepository jogadorRepository;
    private final PartidaMapper mapper;

    public PartidaResponseDTO criar(UUID peladaId, PartidaRequestDTO request) {
        if (!peladaRepository.existsById(peladaId)) {
            throw new ResourceNotFoundException("Pelada nao encontrada: " + peladaId);
        }
        Partida entity = mapper.toEntity(request);
        entity.setPeladaId(peladaId);
        return mapper.toResponse(partidaRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public PartidaResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<PartidaResponseDTO> listarPorPelada(UUID peladaId, Pageable pageable) {
        return partidaRepository.findByPeladaId(peladaId, pageable).map(mapper::toResponse);
    }

    public PartidaResponseDTO atualizar(UUID id, PartidaRequestDTO request) {
        Partida entity = buscarEntidade(id);
        mapper.updateFromRequest(request, entity);
        return mapper.toResponse(partidaRepository.save(entity));
    }

    public PartidaResponseDTO atualizarStatus(UUID id, StatusPartida status) {
        Partida entity = buscarEntidade(id);
        entity.setStatus(status);
        return mapper.toResponse(partidaRepository.save(entity));
    }

    public void deletar(UUID id) {
        Partida entity = buscarEntidade(id);
        entity.setStatus(StatusPartida.CANCELADA);
        partidaRepository.save(entity);
    }

    public ParticipanteResponseDTO adicionarParticipante(UUID partidaId, ParticipanteRequestDTO request) {
        Partida partida = buscarEntidade(partidaId);
        Jogador jogador = jogadorRepository.findById(request.jogadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + request.jogadorId()));

        if (participanteRepository.existsByPartidaIdAndJogadorId(partida.getId(), jogador.getId())) {
            throw new BusinessException("Jogador ja confirmado nesta partida");
        }

        Participante participante = Participante.builder()
                .partidaId(partida.getId())
                .jogadorId(jogador.getId())
                .confirmado(true)
                .goleiro(Boolean.TRUE.equals(request.goleiro()))
                .build();

        return toResponse(participanteRepository.save(participante), jogador);
    }

    @Transactional(readOnly = true)
    public List<ParticipanteResponseDTO> listarParticipantes(UUID partidaId) {
        buscarEntidade(partidaId);
        return participanteRepository.findByPartidaId(partidaId).stream()
                .map(participante -> toResponse(participante, jogadorRepository.findById(participante.getJogadorId()).orElse(null)))
                .toList();
    }

    public ParticipanteResponseDTO atualizarPresenca(UUID partidaId, UUID jogadorId, boolean presente) {
        Participante participante = participanteRepository.findByPartidaIdAndJogadorId(partidaId, jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante nao encontrado"));
        participante.setPresente(presente);
        Jogador jogador = jogadorRepository.findById(jogadorId).orElse(null);
        return toResponse(participanteRepository.save(participante), jogador);
    }

    public void removerParticipante(UUID partidaId, UUID jogadorId) {
        Participante participante = participanteRepository.findByPartidaIdAndJogadorId(partidaId, jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante nao encontrado"));
        participanteRepository.delete(participante);
    }

    Partida buscarEntidade(UUID id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida nao encontrada: " + id));
    }

    private ParticipanteResponseDTO toResponse(Participante participante, Jogador jogador) {
        return new ParticipanteResponseDTO(
                participante.getId(),
                participante.getPartidaId(),
                participante.getJogadorId(),
                jogador != null ? jogador.getNome() : null,
                participante.isConfirmado(),
                participante.isPresente(),
                participante.isGoleiro()
        );
    }
}
