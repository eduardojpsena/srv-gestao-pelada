package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.PartidaService;
import br.com.gestao_pelada.partida.application.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaStatusUpdateDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Partidas", description = "Cadastro e gestao de partidas de uma pelada")
public class PartidaController {

    private final PartidaService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping("/api/v1/peladas/{peladaId}/partidas")
    public ResponseEntity<PartidaResponseDTO> criar(@PathVariable UUID peladaId, @Valid @RequestBody PartidaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(peladaId, request));
    }

    @GetMapping("/api/v1/peladas/{peladaId}/partidas")
    public ResponseEntity<PageResponse<PartidaResponseDTO>> listarPorPelada(@PathVariable UUID peladaId, Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listarPorPelada(peladaId, pageable)));
    }

    @GetMapping("/api/v1/partidas/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PutMapping("/api/v1/partidas/{id}")
    public ResponseEntity<PartidaResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody PartidaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PatchMapping("/api/v1/partidas/{id}/status")
    public ResponseEntity<PartidaResponseDTO> atualizarStatus(@PathVariable UUID id, @Valid @RequestBody PartidaStatusUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request.status()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/api/v1/partidas/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
