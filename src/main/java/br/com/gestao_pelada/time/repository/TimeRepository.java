package br.com.gestao_pelada.time.repository;

import br.com.gestao_pelada.time.model.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findByPartidaId(Long partidaId);

    void deleteByPartidaId(Long partidaId);
}

