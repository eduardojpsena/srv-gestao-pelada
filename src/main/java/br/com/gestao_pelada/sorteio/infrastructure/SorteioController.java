package br.com.gestao_pelada.sorteio.infrastructure;

import br.com.gestao_pelada.sorteio.application.SorteioService;
import br.com.gestao_pelada.sorteio.application.dto.SorteioRequestDTO;
import br.com.gestao_pelada.time.application.dto.TimeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SorteioController implements SorteioApi {

    private final SorteioService service;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<List<TimeResponseDTO>> sortear(UUID partidaId, SorteioRequestDTO request) {
        return ResponseEntity.ok(service.sortear(partidaId, request));
    }
}
