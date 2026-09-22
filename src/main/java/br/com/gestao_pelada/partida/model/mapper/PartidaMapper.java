package br.com.gestao_pelada.partida.model.mapper;

import br.com.gestao_pelada.partida.model.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.model.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.model.entity.Partida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PartidaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peladaId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Partida toEntity(PartidaRequestDTO request);

    PartidaResponseDTO toResponse(Partida entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "peladaId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateFromRequest(PartidaRequestDTO request, @MappingTarget Partida entity);
}

