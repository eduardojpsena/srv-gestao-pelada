package br.com.gestao_pelada.service;

import br.com.gestao_pelada.model.dto.JogadorRequestDTO;
import br.com.gestao_pelada.model.dto.JogadorResponseDTO;
import br.com.gestao_pelada.mapper.JogadorMapper;
import br.com.gestao_pelada.model.entity.JogadorEntity;
import br.com.gestao_pelada.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogadorService {

    private final JogadorRepository repository;
    private final JogadorMapper mapper;

    public JogadorResponseDTO criar(JogadorRequestDTO request) {
        JogadorEntity entity = repository.save(mapper.toEntity(request));
        return mapper.toResponse(entity);
    }

    public JogadorResponseDTO buscarPorId(Long id) {
        JogadorEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado"));
        return mapper.toResponse(entity);
    }

    public List<JogadorResponseDTO> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
