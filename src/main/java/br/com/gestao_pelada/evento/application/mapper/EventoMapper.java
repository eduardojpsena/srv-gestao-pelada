package br.com.gestao_pelada.evento.application.mapper;

import br.com.gestao_pelada.evento.application.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.domain.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "partidaId", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Evento toEntity(EventoRequestDTO request);
}
