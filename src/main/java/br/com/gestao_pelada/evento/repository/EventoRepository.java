package br.com.gestao_pelada.evento.repository;

import br.com.gestao_pelada.evento.model.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByPartidaId(Long partidaId);

    List<Evento> findByPartidaIdIn(List<Long> partidaIds);
}


