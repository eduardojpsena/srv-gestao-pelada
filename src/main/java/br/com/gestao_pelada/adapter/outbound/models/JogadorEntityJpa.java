package br.com.gestao_pelada.adapter.outbound.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="jogadores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JogadorEntityJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String apelido;
    private String telefone;
    private String email;
    private Double nota;
    private String posicao;

}
