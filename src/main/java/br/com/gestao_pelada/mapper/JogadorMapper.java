package br.com.gestao_pelada.mapper;

import br.com.gestao_pelada.adapter.outbound.models.JogadorEntityJpa;
import br.com.gestao_pelada.domain.model.Jogador;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JogadorMapper {

    JogadorEntityJpa toEntity(Jogador jogador);

    Jogador toDomain(JogadorEntityJpa jogadorEntityJpa);
}
