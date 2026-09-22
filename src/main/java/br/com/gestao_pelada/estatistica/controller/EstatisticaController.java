package br.com.gestao_pelada.estatistica.controller;

import br.com.gestao_pelada.estatistica.service.EstatisticaService;
import br.com.gestao_pelada.estatistica.model.dto.RankingJogadorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EstatisticaController implements EstatisticaApi {

    private final EstatisticaService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> ranking(Long peladaId) {
        return ResponseEntity.ok(service.rankingCompleto(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> artilheiros(Long peladaId) {
        return ResponseEntity.ok(service.artilheiros(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> assistencias(Long peladaId) {
        return ResponseEntity.ok(service.assistencias(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> cartoes(Long peladaId) {
        return ResponseEntity.ok(service.cartoes(peladaId));
    }
}

