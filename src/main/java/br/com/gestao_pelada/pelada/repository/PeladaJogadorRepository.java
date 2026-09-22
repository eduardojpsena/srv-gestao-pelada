package br.com.gestao_pelada.pelada.repository;

import br.com.gestao_pelada.pelada.model.entity.PeladaJogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeladaJogadorRepository extends JpaRepository<PeladaJogador, Long> {

    List<PeladaJogador> findByPeladaIdAndAtivoTrue(Long peladaId);

    Optional<PeladaJogador> findByPeladaIdAndJogadorId(Long peladaId, Long jogadorId);

    boolean existsByPeladaIdAndJogadorIdAndAtivoTrue(Long peladaId, Long jogadorId);
}

