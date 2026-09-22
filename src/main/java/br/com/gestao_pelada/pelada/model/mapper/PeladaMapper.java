package br.com.gestao_pelada.pelada.model.mapper;

import br.com.gestao_pelada.pelada.model.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaResponseDTO;
import br.com.gestao_pelada.pelada.model.entity.Pelada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PeladaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizadorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Pelada toEntity(PeladaRequestDTO request);

    PeladaResponseDTO toResponse(Pelada entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizadorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateFromRequest(PeladaRequestDTO request, @MappingTarget Pelada entity);
}

