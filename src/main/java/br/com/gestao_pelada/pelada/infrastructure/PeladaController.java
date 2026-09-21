package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.application.PeladaService;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaResponseDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PeladaController implements PeladaApi {

    private final PeladaService service;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PeladaResponseDTO> criar(PeladaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @Override
    public ResponseEntity<PeladaResponseDTO> buscarPorId(UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PageResponse<PeladaResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PeladaResponseDTO> atualizar(UUID id, PeladaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<PeladaJogadorResponseDTO> adicionarJogador(UUID id, PeladaJogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarJogador(id, request));
    }

    @Override
    public ResponseEntity<List<PeladaJogadorResponseDTO>> listarJogadores(UUID id) {
        return ResponseEntity.ok(service.listarJogadores(id));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> removerJogador(UUID id, UUID jogadorId) {
        service.removerJogador(id, jogadorId);
        return ResponseEntity.noContent().build();
    }
}
