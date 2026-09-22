package br.com.gestao_pelada.pelada.repository;

import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.entity.PeladaMembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeladaMembroRepository extends JpaRepository<PeladaMembro, Long> {

    Optional<PeladaMembro> findByPeladaIdAndUsuarioId(Long peladaId, Long usuarioId);

    boolean existsByPeladaIdAndUsuarioIdAndAtivoTrue(Long peladaId, Long usuarioId);

    long countByPeladaIdAndPapelAndAtivoTrue(Long peladaId, PapelPelada papel);

    Page<PeladaMembro> findByUsuarioIdAndAtivoTrue(Long usuarioId, Pageable pageable);
}

