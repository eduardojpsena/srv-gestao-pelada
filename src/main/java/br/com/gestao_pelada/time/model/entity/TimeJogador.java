package br.com.gestao_pelada.time.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "time_jogadores")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_id", nullable = false)
    private Long timeId;

    @Column(name = "jogador_id", nullable = false)
    private Long jogadorId;

    @Column(nullable = false)
    @Builder.Default
    private boolean goleiro = false;
}

