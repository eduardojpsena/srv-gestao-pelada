package br.com.gestao_pelada.partida.model.entity;

import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Column;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Entity;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.EnumType;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Enumerated;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.GeneratedValue;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.GenerationType;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Id;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.PrePersist;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "partidas")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pelada_id", nullable = false)
    private Long peladaId;

    @Column(nullable = false)
    private LocalDate data;

    private LocalTime horario;

    private String local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusPartida status = StatusPartida.AGENDADA;

    @Column(name = "numero_times", nullable = false)
    @Builder.Default
    private Integer numeroTimes = 2;

    @Column(name = "jogadores_por_time")
    private Integer jogadoresPorTime;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
    }
}

