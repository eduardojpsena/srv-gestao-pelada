package br.com.gestao_pelada.jogador.infrastructure;

import br.com.gestao_pelada.jogador.application.JogadorService;
import br.com.gestao_pelada.jogador.application.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.application.dto.JogadorResponseDTO;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jogadores")
@RequiredArgsConstructor
@Tag(name = "Jogadores", description = "Cadastro e gestao de jogadores")
public class JogadorController {

    private final JogadorService service;

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PostMapping
    public ResponseEntity<JogadorResponseDTO> criar(@Valid @RequestBody JogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogadorResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<JogadorResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<JogadorResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody JogadorRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
