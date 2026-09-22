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
@Table(name = "times")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partida_id", nullable = false)
    private Long partidaId;

    @Column(nullable = false)
    private String nome;

    private String cor;

    @Column(name = "nota_total")
    private Double notaTotal;
}

