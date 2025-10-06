package br.com.gestao_pelada.adapter.outbound.repositories;

import br.com.gestao_pelada.adapter.outbound.models.JogadorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepositoryJpa extends JpaRepository<JogadorEntityJpa, Long> {
}
