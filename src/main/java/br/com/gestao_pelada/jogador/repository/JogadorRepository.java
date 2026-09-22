package br.com.gestao_pelada.jogador.repository;

import br.com.gestao_pelada.jogador.model.entity.Jogador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    Page<Jogador> findByAtivoTrue(Pageable pageable);

    List<Jogador> findByIdIn(List<Long> ids);

    Optional<Jogador> findByUsuarioId(Long usuarioId);

    Optional<Jogador> findByEmailIgnoreCase(String email);
}

