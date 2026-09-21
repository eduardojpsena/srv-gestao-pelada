package br.com.gestao_pelada.partida.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "participantes", uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "jogador_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "partida_id", nullable = false)
    private UUID partidaId;

    @Column(name = "jogador_id", nullable = false)
    private UUID jogadorId;

    @Column(nullable = false)
    @Builder.Default
    private boolean confirmado = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean presente = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean goleiro = false;
}
