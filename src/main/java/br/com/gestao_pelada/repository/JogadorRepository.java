package br.com.gestao_pelada.repository;

import br.com.gestao_pelada.model.entity.JogadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<JogadorEntity, Long> {
}
