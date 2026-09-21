package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.domain.Pelada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeladaRepository extends JpaRepository<Pelada, UUID> {

    Page<Pelada> findAllByOrganizadorId(UUID organizadorId, Pageable pageable);
}
