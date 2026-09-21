package br.com.gestao_pelada.time.infrastructure;

import br.com.gestao_pelada.time.domain.TimeJogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeJogadorRepository extends JpaRepository<TimeJogador, UUID> {

    List<TimeJogador> findByTimeId(UUID timeId);

    List<TimeJogador> findByTimeIdIn(List<UUID> timeIds);

    void deleteByTimeIdIn(List<UUID> timeIds);
}
