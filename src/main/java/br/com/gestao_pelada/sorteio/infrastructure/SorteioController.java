package br.com.gestao_pelada.sorteio.infrastructure;

import br.com.gestao_pelada.sorteio.application.SorteioService;
import br.com.gestao_pelada.sorteio.application.dto.SorteioRequestDTO;
import br.com.gestao_pelada.time.application.dto.TimeResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/partidas/{partidaId}/sorteio")
@RequiredArgsConstructor
@Tag(name = "Sorteio", description = "Sorteio de times por potes, estrelas ou avulso")
public class SorteioController {

    private final SorteioService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping
    public ResponseEntity<List<TimeResponseDTO>> sortear(@PathVariable UUID partidaId, @Valid @RequestBody SorteioRequestDTO request) {
        return ResponseEntity.ok(service.sortear(partidaId, request));
    }
}
