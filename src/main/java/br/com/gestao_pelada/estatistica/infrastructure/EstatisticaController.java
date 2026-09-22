package br.com.gestao_pelada.estatistica.infrastructure;

import br.com.gestao_pelada.estatistica.application.EstatisticaService;
import br.com.gestao_pelada.estatistica.application.dto.RankingJogadorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EstatisticaController implements EstatisticaApi {

    private final EstatisticaService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> ranking(UUID peladaId) {
        return ResponseEntity.ok(service.rankingCompleto(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> artilheiros(UUID peladaId) {
        return ResponseEntity.ok(service.artilheiros(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> assistencias(UUID peladaId) {
        return ResponseEntity.ok(service.assistencias(peladaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessar(#peladaId, authentication)")
    public ResponseEntity<List<RankingJogadorDTO>> cartoes(UUID peladaId) {
        return ResponseEntity.ok(service.cartoes(peladaId));
    }
}
