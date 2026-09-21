package br.com.gestao_pelada.time.infrastructure;

import br.com.gestao_pelada.time.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeRepository extends JpaRepository<Time, UUID> {

    List<Time> findByPartidaId(UUID partidaId);

    void deleteByPartidaId(UUID partidaId);
}
