package br.com.gestao_pelada.jogador.infrastructure;

import br.com.gestao_pelada.jogador.domain.Jogador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JogadorRepository extends JpaRepository<Jogador, UUID> {

    Page<Jogador> findByAtivoTrue(Pageable pageable);

    List<Jogador> findByIdIn(List<UUID> ids);
}
