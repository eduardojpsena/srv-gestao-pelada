package br.com.gestao_pelada.application.service;

import br.com.gestao_pelada.adapter.inbound.models.JogadorRequestDTO;
import br.com.gestao_pelada.application.port.inbound.JogadorUseCase;
import br.com.gestao_pelada.application.port.outbound.JogadorRepositoryPort;
import br.com.gestao_pelada.domain.model.Jogador;
import br.com.gestao_pelada.mapper.JogadorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogadorService implements JogadorUseCase {

    private final JogadorRepositoryPort repository;
    private final JogadorMapper mapper;

    @Override
    public Jogador criarJogador(JogadorRequestDTO jogadorRequest) {
        Jogador newPlayer = mapper.dtoToDomain(jogadorRequest);
        return this.repository.save(newPlayer);
    }

    @Override
    public Jogador buscarJogador(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado"));
    }

    @Override
    public List<Jogador> listarJogadores() {
        return this.repository.findAll();
    }

    @Override
    public void deletarJogador(Long id) {
        this.repository.deleteById(id);
    }
}
