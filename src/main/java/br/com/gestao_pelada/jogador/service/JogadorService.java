package br.com.gestao_pelada.jogador.service;

import br.com.gestao_pelada.jogador.model.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.model.dto.JogadorResponseDTO;
import br.com.gestao_pelada.jogador.model.mapper.JogadorMapper;
import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.jogador.repository.JogadorRepository;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public JogadorResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<JogadorResponseDTO> listar(Pageable pageable) {
        return repository.findByAtivoTrue(pageable).map(mapper::toResponse);
    }

    public JogadorResponseDTO atualizar(Long id, JogadorRequestDTO request) {
        Jogador entity = buscarEntidade(id);
        mapper.updateFromRequest(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    public void deletar(Long id) {
        Jogador entity = buscarEntidade(id);
        entity.setAtivo(false);
        repository.save(entity);
    }

    Jogador buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + id));
    }
}

