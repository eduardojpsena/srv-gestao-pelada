package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.domain.PeladaJogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PeladaJogadorRepository extends JpaRepository<PeladaJogador, UUID> {

    List<PeladaJogador> findByPeladaIdAndAtivoTrue(UUID peladaId);

    Optional<PeladaJogador> findByPeladaIdAndJogadorId(UUID peladaId, UUID jogadorId);

    boolean existsByPeladaIdAndJogadorIdAndAtivoTrue(UUID peladaId, UUID jogadorId);
}
