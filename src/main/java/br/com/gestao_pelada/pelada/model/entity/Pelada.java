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
import java.time.LocalTime;

@Entity
@Table(name = "peladas")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pelada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", length = 20)
    private DiaSemana diaSemana;

    private LocalTime horario;

    private String local;

    @Column(name = "organizador_id", nullable = false)
    private Long organizadorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusPelada status = StatusPelada.ATIVA;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
    }
}

