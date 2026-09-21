package br.com.gestao_pelada.evento.infrastructure;

import br.com.gestao_pelada.evento.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {

    List<Evento> findByPartidaId(UUID partidaId);

    List<Evento> findByPartidaIdIn(List<UUID> partidaIds);
}

