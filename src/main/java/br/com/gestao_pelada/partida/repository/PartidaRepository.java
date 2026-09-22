package br.com.gestao_pelada.partida.repository;

import br.com.gestao_pelada.partida.model.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    Page<Partida> findByPeladaId(Long peladaId, Pageable pageable);

    List<Partida> findAllByPeladaId(Long peladaId);
}


