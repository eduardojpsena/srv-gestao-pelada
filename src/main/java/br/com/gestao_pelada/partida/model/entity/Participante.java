package br.com.gestao_pelada.partida.model.entity;

import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Column;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Entity;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.GeneratedValue;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.GenerationType;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Id;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.Table;
import br.com.gestao_pelada.partida.model.enums.StatusPartida;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "participantes", uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "jogador_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partida_id", nullable = false)
    private Long partidaId;

    @Column(name = "jogador_id", nullable = false)
    private Long jogadorId;

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

