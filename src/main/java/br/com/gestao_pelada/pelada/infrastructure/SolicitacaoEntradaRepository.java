package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.domain.SolicitacaoEntrada;
import br.com.gestao_pelada.pelada.domain.StatusSolicitacaoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoEntradaRepository extends JpaRepository<SolicitacaoEntrada, UUID> {

    boolean existsByPeladaIdAndUsuarioIdAndStatus(
            UUID peladaId, UUID usuarioId, StatusSolicitacaoEntrada status);

    List<SolicitacaoEntrada> findByPeladaIdAndStatus(UUID peladaId, StatusSolicitacaoEntrada status);
}
