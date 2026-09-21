package br.com.gestao_pelada.jogador.application;

import br.com.gestao_pelada.jogador.application.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.application.dto.JogadorResponseDTO;
import br.com.gestao_pelada.jogador.application.mapper.JogadorMapper;
import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JogadorService {

    private final JogadorRepository repository;
    private final JogadorMapper mapper;

    public JogadorResponseDTO criar(JogadorRequestDTO request) {
        Jogador entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public JogadorResponseDTO buscarPorId(UUID id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<JogadorResponseDTO> listar(Pageable pageable) {
        return repository.findByAtivoTrue(pageable).map(mapper::toResponse);
    }

    public JogadorResponseDTO atualizar(UUID id, JogadorRequestDTO request) {
        Jogador entity = buscarEntidade(id);
        mapper.updateFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    public void deletar(UUID id) {
        Jogador entity = buscarEntidade(id);
        entity.setAtivo(false);
        repository.save(entity);
    }

    Jogador buscarEntidade(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + id));
    }
}
