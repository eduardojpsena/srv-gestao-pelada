package br.com.gestao_pelada.evento.infrastructure;

import br.com.gestao_pelada.evento.application.EventoService;
import br.com.gestao_pelada.evento.application.dto.EventoRequestDTO;
import br.com.gestao_pelada.evento.application.dto.EventoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Eventos de partida: gols, assistencias e cartoes")
public class EventoController {

    private final EventoService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping("/api/v1/partidas/{partidaId}/eventos")
    public ResponseEntity<EventoResponseDTO> registrar(@PathVariable UUID partidaId, @Valid @RequestBody EventoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(partidaId, request));
    }

    @GetMapping("/api/v1/partidas/{partidaId}/eventos")
    public ResponseEntity<List<EventoResponseDTO>> listar(@PathVariable UUID partidaId) {
        return ResponseEntity.ok(service.listarPorPartida(partidaId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/api/v1/eventos/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
