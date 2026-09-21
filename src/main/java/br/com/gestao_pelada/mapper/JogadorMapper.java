package br.com.gestao_pelada.mapper;

import br.com.gestao_pelada.model.dto.JogadorRequestDTO;
import br.com.gestao_pelada.model.dto.JogadorResponseDTO;
import br.com.gestao_pelada.model.entity.JogadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JogadorMapper {

    @Mapping(target = "id", ignore = true)
    JogadorEntity toEntity(JogadorRequestDTO request);

    JogadorResponseDTO toResponse(JogadorEntity entity);
}
