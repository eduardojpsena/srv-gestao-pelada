package br.com.gestao_pelada.time.infrastructure;

import br.com.gestao_pelada.time.application.TimeService;
import br.com.gestao_pelada.time.application.dto.TimeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TimeController implements TimeApi {

    private final TimeService service;

    @Override
    public ResponseEntity<List<TimeResponseDTO>> listar(UUID partidaId) {
        return ResponseEntity.ok(service.listarPorPartida(partidaId));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> remover(UUID partidaId) {
        service.removerTimesDaPartida(partidaId);
        return ResponseEntity.noContent().build();
    }
}
