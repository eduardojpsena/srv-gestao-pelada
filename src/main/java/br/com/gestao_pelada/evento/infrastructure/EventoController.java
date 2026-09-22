package br.com.gestao_pelada.evento.infrastructure;

import br.com.gestao_pelada.evento.application.EventoService;
import br.com.gestao_pelada.evento.application.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.application.dto.EventoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventoController implements EventoApi {

    private final EventoService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<EventoResponseDTO> registrar(UUID partidaId, EventoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(partidaId, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessarPartida(#partidaId, authentication)")
    public ResponseEntity<List<EventoResponseDTO>> listar(UUID partidaId) {
        return ResponseEntity.ok(service.listarPorPartida(partidaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarEvento(#id, authentication)")
    public ResponseEntity<Void> remover(UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
