package br.com.gestao_pelada.application.port.outbound;

import br.com.gestao_pelada.domain.model.Jogador;

import java.util.List;
import java.util.Optional;

public interface JogadorRepositoryPort {

    Jogador save(Jogador jogador);
    Optional<Jogador> findById(Long id);
    List<Jogador> findAll();
    void deleteById(Long id);
}
