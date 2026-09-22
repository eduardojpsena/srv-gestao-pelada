package br.com.gestao_pelada.partida.repository;

import br.com.gestao_pelada.partida.model.entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    List<Participante> findByPartidaId(Long partidaId);

    List<Participante> findByPartidaIdAndConfirmadoTrue(Long partidaId);

    Optional<Participante> findByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);

    boolean existsByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);

    List<Participante> findByPartidaIdIn(List<Long> partidaIds);
}


