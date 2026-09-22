package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.domain.PapelPelada;
import br.com.gestao_pelada.pelada.domain.PeladaMembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PeladaMembroRepository extends JpaRepository<PeladaMembro, UUID> {

    Optional<PeladaMembro> findByPeladaIdAndUsuarioId(UUID peladaId, UUID usuarioId);

    boolean existsByPeladaIdAndUsuarioIdAndAtivoTrue(UUID peladaId, UUID usuarioId);

    long countByPeladaIdAndPapelAndAtivoTrue(UUID peladaId, PapelPelada papel);

    Page<PeladaMembro> findByUsuarioIdAndAtivoTrue(UUID usuarioId, Pageable pageable);
}
