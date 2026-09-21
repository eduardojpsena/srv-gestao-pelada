package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipanteRepository extends JpaRepository<Participante, UUID> {

    List<Participante> findByPartidaId(UUID partidaId);

    List<Participante> findByPartidaIdAndConfirmadoTrue(UUID partidaId);

    Optional<Participante> findByPartidaIdAndJogadorId(UUID partidaId, UUID jogadorId);

    boolean existsByPartidaIdAndJogadorId(UUID partidaId, UUID jogadorId);

    List<Participante> findByPartidaIdIn(List<UUID> partidaIds);
}

