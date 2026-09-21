package br.com.gestao_pelada.pelada.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pelada_jogadores", uniqueConstraints = @UniqueConstraint(columnNames = {"pelada_id", "jogador_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeladaJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pelada_id", nullable = false)
    private UUID peladaId;

    @Column(name = "jogador_id", nullable = false)
    private UUID jogadorId;

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
