package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.PartidaService;
import br.com.gestao_pelada.partida.application.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PartidaStatusUpdateDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PartidaController implements PartidaApi {

    private final PartidaService service;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PartidaResponseDTO> criar(UUID peladaId, PartidaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(peladaId, request));
    }

    @Override
    public ResponseEntity<PageResponse<PartidaResponseDTO>> listarPorPelada(UUID peladaId, Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listarPorPelada(peladaId, pageable)));
    }

    @Override
    public ResponseEntity<PartidaResponseDTO> buscarPorId(UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PartidaResponseDTO> atualizar(UUID id, PartidaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PartidaResponseDTO> atualizarStatus(UUID id, PartidaStatusUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request.status()));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
