package br.com.gestao_pelada.jogador.infrastructure;

import br.com.gestao_pelada.jogador.application.JogadorService;
import br.com.gestao_pelada.jogador.application.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.application.dto.JogadorResponseDTO;
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
public class JogadorController implements JogadorApi {

    private final JogadorService service;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<JogadorResponseDTO> criar(JogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @Override
    public ResponseEntity<JogadorResponseDTO> buscarPorId(UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PageResponse<JogadorResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<JogadorResponseDTO> atualizar(UUID id, JogadorRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
