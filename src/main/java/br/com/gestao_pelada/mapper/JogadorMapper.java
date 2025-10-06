package br.com.gestao_pelada.mapper;

import br.com.gestao_pelada.adapter.inbound.models.JogadorRequestDTO;
import br.com.gestao_pelada.adapter.outbound.models.JogadorEntityJpa;
import br.com.gestao_pelada.domain.model.Jogador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface JogadorMapper {

    JogadorEntityJpa domainToEntityJpa(Jogador jogador);

    Jogador EntityJpaToDomain(JogadorEntityJpa jogadorEntityJpa);

    @Mappings({@Mapping(target = "id", ignore = true)})
    Jogador dtoToDomain(JogadorRequestDTO requestDTO);
}
