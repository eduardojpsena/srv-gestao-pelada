package br.com.gestao_pelada.pelada.repository;

import br.com.gestao_pelada.pelada.model.entity.Pelada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PeladaRepository extends JpaRepository<Pelada, Long> {

    Page<Pelada> findAllByOrganizadorId(Long organizadorId, Pageable pageable);
}

