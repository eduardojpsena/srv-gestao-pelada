package br.com.gestao_pelada.sorteio.controller;

import br.com.gestao_pelada.sorteio.service.SorteioService;
import br.com.gestao_pelada.sorteio.model.dto.SorteioRequestDTO;
import br.com.gestao_pelada.time.model.dto.TimeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SorteioController implements SorteioApi {

    private final SorteioService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<List<TimeResponseDTO>> sortear(Long partidaId, SorteioRequestDTO request) {
        return ResponseEntity.ok(service.sortear(partidaId, request));
    }
}

