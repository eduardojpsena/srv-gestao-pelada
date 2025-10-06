package br.com.gestao_pelada.adapter.outbound.repositories;

import br.com.gestao_pelada.adapter.outbound.models.JogadorEntityJpa;
import br.com.gestao_pelada.application.port.outbound.JogadorRepositoryPort;
import br.com.gestao_pelada.domain.model.Jogador;
import br.com.gestao_pelada.mapper.JogadorMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JogadorRepositoryAdapter implements JogadorRepositoryPort {

    private final JogadorRepositoryJpa repositoryJpa;
    private final JogadorMapper mapper;

    public JogadorRepositoryAdapter(JogadorRepositoryJpa repositoryJpa, JogadorMapper mapper) {
        this.repositoryJpa = repositoryJpa;
        this.mapper = mapper;
    }

    @Override
    public Jogador save(Jogador jogador) {
        JogadorEntityJpa jogadorEntityJpa = mapper.domainToEntityJpa(jogador);
        return mapper.EntityJpaToDomain(this.repositoryJpa.save(jogadorEntityJpa));
    }

    @Override
    public Optional<Jogador> findById(Long id) {
        Optional<JogadorEntityJpa> jogadorEntityJpa = this.repositoryJpa.findById(id);
        return jogadorEntityJpa
                .map(mapper::EntityJpaToDomain);
    }

    @Override
    public List<Jogador> findAll() {
        List<JogadorEntityJpa> jogadorEntityJpa = this.repositoryJpa.findAll();
        return jogadorEntityJpa
                .stream()
                .map(mapper::EntityJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.repositoryJpa.deleteById(id);
    }
}
