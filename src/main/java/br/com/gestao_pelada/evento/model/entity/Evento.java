package br.com.gestao_pelada.evento.model.entity;

import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.Column;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.Entity;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.EnumType;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.Enumerated;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.GeneratedValue;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.GenerationType;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.Id;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.PrePersist;
import br.com.gestao_pelada.evento.model.enums.TipoEvento;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partida_id", nullable = false)
    private Long partidaId;

    @Column(name = "time_id")
    private Long timeId;

    @Column(name = "jogador_id", nullable = false)
    private Long jogadorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEvento tipo;

    private Integer minuto;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
    }
}

