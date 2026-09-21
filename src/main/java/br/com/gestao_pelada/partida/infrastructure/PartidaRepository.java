package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.domain.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    Page<Partida> findByPeladaId(UUID peladaId, Pageable pageable);

    List<Partida> findAllByPeladaId(UUID peladaId);
}

