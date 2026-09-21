package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.application.PeladaService;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaResponseDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/peladas")
@RequiredArgsConstructor
@Tag(name = "Peladas", description = "Cadastro e gestao de peladas e seus jogadores")
public class PeladaController {

    private final PeladaService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping
    public ResponseEntity<PeladaResponseDTO> criar(@Valid @RequestBody PeladaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeladaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PeladaResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PeladaResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody PeladaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping("/{id}/jogadores")
    public ResponseEntity<PeladaJogadorResponseDTO> adicionarJogador(
            @PathVariable UUID id, @Valid @RequestBody PeladaJogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarJogador(id, request));
    }

    @GetMapping("/{id}/jogadores")
    public ResponseEntity<List<PeladaJogadorResponseDTO>> listarJogadores(@PathVariable UUID id) {
        return ResponseEntity.ok(service.listarJogadores(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/{id}/jogadores/{jogadorId}")
    public ResponseEntity<Void> removerJogador(@PathVariable UUID id, @PathVariable UUID jogadorId) {
        service.removerJogador(id, jogadorId);
        return ResponseEntity.noContent().build();
    }
}
