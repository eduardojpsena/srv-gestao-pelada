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
import br.com.gestao_pelada.pelada.model.enums.DiaSemana;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusPelada;
import br.com.gestao_pelada.pelada.model.enums.StatusSolicitacaoEntrada;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "pelada_jogadores", uniqueConstraints = @UniqueConstraint(columnNames = {"pelada_id", "jogador_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeladaJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pelada_id", nullable = false)
    private Long peladaId;

    @Column(name = "jogador_id", nullable = false)
    private Long jogadorId;

    @Column(name = "nota_pelada")
    private Double notaPelada;

    @Column(nullable = false)
    @Builder.Default
    private boolean mensalista = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @Column(name = "data_entrada", nullable = false, updatable = false)
    private Instant dataEntrada;

    @PrePersist
    public void prePersist() {
        if (dataEntrada == null) {
            dataEntrada = Instant.now();
        }
    }
}

