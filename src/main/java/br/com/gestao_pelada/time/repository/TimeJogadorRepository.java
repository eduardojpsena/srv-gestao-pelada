package br.com.gestao_pelada.time.repository;

import br.com.gestao_pelada.time.model.entity.TimeJogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeJogadorRepository extends JpaRepository<TimeJogador, Long> {

    List<TimeJogador> findByTimeId(Long timeId);

    List<TimeJogador> findByTimeIdIn(List<Long> timeIds);

    void deleteByTimeIdIn(List<Long> timeIds);
}

