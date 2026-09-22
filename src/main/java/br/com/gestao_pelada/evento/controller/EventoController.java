package br.com.gestao_pelada.evento.controller;

import br.com.gestao_pelada.evento.service.EventoService;
import br.com.gestao_pelada.evento.model.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.model.dto.EventoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventoController implements EventoApi {

    private final EventoService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<EventoResponseDTO> registrar(Long partidaId, EventoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(partidaId, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessarPartida(#partidaId, authentication)")
    public ResponseEntity<List<EventoResponseDTO>> listar(Long partidaId) {
        return ResponseEntity.ok(service.listarPorPartida(partidaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarEvento(#id, authentication)")
    public ResponseEntity<Void> remover(Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}

