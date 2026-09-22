package br.com.gestao_pelada.pelada.repository;

import br.com.gestao_pelada.pelada.model.entity.SolicitacaoEntrada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoEntradaRepository extends JpaRepository<SolicitacaoEntrada, Long> {

    boolean existsByPeladaIdAndUsuarioIdAndStatus(
            Long peladaId, Long usuarioId, StatusSolicitacaoEntrada status);

    List<SolicitacaoEntrada> findByPeladaIdAndStatus(Long peladaId, StatusSolicitacaoEntrada status);
}

