package br.com.gestao_pelada.jogador.application.mapper;

import br.com.gestao_pelada.jogador.application.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.application.dto.JogadorResponseDTO;
import br.com.gestao_pelada.jogador.domain.Jogador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JogadorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Jogador toEntity(JogadorRequestDTO request);

    JogadorResponseDTO toResponse(Jogador entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateFromRequest(JogadorRequestDTO request, @MappingTarget Jogador entity);
}
