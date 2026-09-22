package br.com.gestao_pelada.jogador.model.entity;

import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.Column;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.Entity;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.EnumType;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.Enumerated;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.GeneratedValue;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.GenerationType;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.Id;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.PrePersist;
import br.com.gestao_pelada.jogador.model.enums.Posicao;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "jogadores")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;

    @Column(nullable = false)
    private String nome;

    private String apelido;
    private String telefone;
    private String email;

    @Column(name = "nota_geral", nullable = false)
    private Double notaGeral;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Posicao posicao;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
        if (notaGeral == null) {
            notaGeral = 3.0;
        }
    }
}

