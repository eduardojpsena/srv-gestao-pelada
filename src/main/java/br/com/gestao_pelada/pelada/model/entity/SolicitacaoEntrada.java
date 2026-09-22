package br.com.gestao_pelada.pelada.model.entity;

import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.Column;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.Entity;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.EnumType;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.Enumerated;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.GeneratedValue;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.GenerationType;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.Id;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.PrePersist;
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "solicitacoes_entrada")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pelada_id", nullable = false)
    private Long peladaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusSolicitacaoEntrada status = StatusSolicitacaoEntrada.PENDENTE;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "decidido_em")
    private Instant decididoEm;

    @Column(name = "decidido_por")
    private Long decididoPor;

    @PrePersist
    void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
    }
}

