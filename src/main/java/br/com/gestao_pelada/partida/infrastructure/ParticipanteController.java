package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.PartidaService;
import br.com.gestao_pelada.partida.application.dto.ParticipanteRequestDTO;
import br.com.gestao_pelada.partida.application.dto.ParticipanteResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PresencaUpdateDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/partidas/{partidaId}/participantes")
@RequiredArgsConstructor
@Tag(name = "Participantes", description = "Confirmacao de presenca de jogadores em uma partida")
public class ParticipanteController {

    private final PartidaService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping
    public ResponseEntity<ParticipanteResponseDTO> adicionar(
            @PathVariable UUID partidaId, @Valid @RequestBody ParticipanteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarParticipante(partidaId, request));
    }

    @GetMapping
    public ResponseEntity<List<ParticipanteResponseDTO>> listar(@PathVariable UUID partidaId) {
        return ResponseEntity.ok(service.listarParticipantes(partidaId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PatchMapping("/{jogadorId}/presenca")
    public ResponseEntity<ParticipanteResponseDTO> atualizarPresenca(
            @PathVariable UUID partidaId, @PathVariable UUID jogadorId, @Valid @RequestBody PresencaUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarPresenca(partidaId, jogadorId, request.presente()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/{jogadorId}")
    public ResponseEntity<Void> remover(@PathVariable UUID partidaId, @PathVariable UUID jogadorId) {
        service.removerParticipante(partidaId, jogadorId);
        return ResponseEntity.noContent().build();
    }
}
