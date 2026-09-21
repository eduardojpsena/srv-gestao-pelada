package br.com.gestao_pelada.time.infrastructure;

import br.com.gestao_pelada.time.application.TimeService;
import br.com.gestao_pelada.time.application.dto.TimeResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/partidas/{partidaId}/times")
@RequiredArgsConstructor
@Tag(name = "Times", description = "Times formados para uma partida")
public class TimeController {

    private final TimeService service;

    @GetMapping
    public ResponseEntity<List<TimeResponseDTO>> listar(@PathVariable UUID partidaId) {
        return ResponseEntity.ok(service.listarPorPartida(partidaId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping
    public ResponseEntity<Void> remover(@PathVariable UUID partidaId) {
        service.removerTimesDaPartida(partidaId);
        return ResponseEntity.noContent().build();
    }
}
