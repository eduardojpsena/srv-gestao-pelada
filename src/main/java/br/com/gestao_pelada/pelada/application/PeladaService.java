package br.com.gestao_pelada.pelada.application;

import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaResponseDTO;
import br.com.gestao_pelada.pelada.application.mapper.PeladaMapper;
import br.com.gestao_pelada.pelada.domain.Pelada;
import br.com.gestao_pelada.pelada.domain.PeladaJogador;
import br.com.gestao_pelada.pelada.infrastructure.PeladaJogadorRepository;
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
public class PeladaService {

    private final PeladaRepository peladaRepository;
    private final PeladaJogadorRepository peladaJogadorRepository;
    private final JogadorRepository jogadorRepository;
    private final PeladaMapper mapper;

    public PeladaResponseDTO criar(PeladaRequestDTO request) {
        Pelada entity = mapper.toEntity(request);
        return mapper.toResponse(peladaRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public PeladaResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<PeladaResponseDTO> listar(Pageable pageable) {
        return peladaRepository.findAll(pageable).map(mapper::toResponse);
    }

    public PeladaResponseDTO atualizar(UUID id, PeladaRequestDTO request) {
        Pelada entity = buscarEntidade(id);
        mapper.updateFromRequest(request, entity);
        return mapper.toResponse(peladaRepository.save(entity));
    }

    public void deletar(UUID id) {
        Pelada entity = buscarEntidade(id);
        entity.setStatus(br.com.gestao_pelada.pelada.domain.StatusPelada.INATIVA);
        peladaRepository.save(entity);
    }

    public PeladaJogadorResponseDTO adicionarJogador(UUID peladaId, PeladaJogadorRequestDTO request) {
        Pelada pelada = buscarEntidade(peladaId);
        Jogador jogador = jogadorRepository.findById(request.jogadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + request.jogadorId()));

        if (peladaJogadorRepository.existsByPeladaIdAndJogadorIdAndAtivoTrue(pelada.getId(), jogador.getId())) {
            throw new BusinessException("Jogador ja participa desta pelada");
        }

        PeladaJogador vinculo = peladaJogadorRepository.findByPeladaIdAndJogadorId(pelada.getId(), jogador.getId())
                .orElseGet(() -> PeladaJogador.builder()
                        .peladaId(pelada.getId())
                        .jogadorId(jogador.getId())
                        .build());

        vinculo.setAtivo(true);
        vinculo.setNotaPelada(request.notaPelada());
        vinculo.setMensalista(Boolean.TRUE.equals(request.mensalista()));

        PeladaJogador salvo = peladaJogadorRepository.save(vinculo);
        return toResponse(salvo, jogador);
    }

    @Transactional(readOnly = true)
    public List<PeladaJogadorResponseDTO> listarJogadores(UUID peladaId) {
        buscarEntidade(peladaId);
        return peladaJogadorRepository.findByPeladaIdAndAtivoTrue(peladaId).stream()
                .map(vinculo -> {
                    Jogador jogador = jogadorRepository.findById(vinculo.getJogadorId())
                            .orElse(null);
                    return toResponse(vinculo, jogador);
                })
                .toList();
    }

    public void removerJogador(UUID peladaId, UUID jogadorId) {
        PeladaJogador vinculo = peladaJogadorRepository.findByPeladaIdAndJogadorId(peladaId, jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao vinculado a esta pelada"));
        vinculo.setAtivo(false);
        peladaJogadorRepository.save(vinculo);
    }

    Pelada buscarEntidade(UUID id) {
        return peladaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pelada nao encontrada: " + id));
    }

    private PeladaJogadorResponseDTO toResponse(PeladaJogador vinculo, Jogador jogador) {
        return new PeladaJogadorResponseDTO(
                vinculo.getId(),
                vinculo.getPeladaId(),
                vinculo.getJogadorId(),
                jogador != null ? jogador.getNome() : null,
                vinculo.getNotaPelada(),
                vinculo.isMensalista(),
                vinculo.isAtivo()
        );
    }
}
