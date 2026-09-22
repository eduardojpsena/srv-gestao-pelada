package br.com.gestao_pelada.partida.controller;

import br.com.gestao_pelada.partida.service.PartidaService;
import br.com.gestao_pelada.partida.model.dto.PartidaRequestDTO;
import br.com.gestao_pelada.partida.model.dto.PartidaResponseDTO;
import br.com.gestao_pelada.partida.model.dto.PartidaStatusUpdateDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PartidaController implements PartidaApi {

    private final PartidaService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciar(#peladaId, authentication)")
    public ResponseEntity<PartidaResponseDTO> criar(Long peladaId, PartidaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(peladaId, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<PageResponse<PartidaResponseDTO>> listarPorPelada(Long peladaId, Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listarPorPelada(peladaId, pageable)));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessarPartida(#id, authentication)")
    public ResponseEntity<PartidaResponseDTO> buscarPorId(Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#id, authentication)")
    public ResponseEntity<PartidaResponseDTO> atualizar(Long id, PartidaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#id, authentication)")
    public ResponseEntity<PartidaResponseDTO> atualizarStatus(Long id, PartidaStatusUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request.status()));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#id, authentication)")
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

