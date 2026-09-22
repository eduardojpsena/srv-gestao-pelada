package br.com.gestao_pelada.jogador.controller;

import br.com.gestao_pelada.jogador.service.JogadorService;
import br.com.gestao_pelada.jogador.model.dto.JogadorRequestDTO;
import br.com.gestao_pelada.jogador.model.dto.JogadorResponseDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class JogadorController implements JogadorApi {

    private final JogadorService service;

    @Override
    @PreAuthorize("denyAll()")
    public ResponseEntity<JogadorResponseDTO> criar(JogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @Override
    public ResponseEntity<JogadorResponseDTO> buscarPorId(Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PageResponse<JogadorResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @Override
    @PreAuthorize("denyAll()")
    public ResponseEntity<JogadorResponseDTO> atualizar(Long id, JogadorRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    @PreAuthorize("denyAll()")
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

