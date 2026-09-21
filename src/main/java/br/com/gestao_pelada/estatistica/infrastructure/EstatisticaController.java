package br.com.gestao_pelada.estatistica.infrastructure;

import br.com.gestao_pelada.estatistica.application.EstatisticaService;
import br.com.gestao_pelada.estatistica.application.dto.RankingJogadorDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/peladas/{peladaId}/estatisticas")
@RequiredArgsConstructor
@Tag(name = "Estatisticas", description = "Rankings e estatisticas dos jogadores de uma pelada")
public class EstatisticaController {

    private final EstatisticaService service;

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingJogadorDTO>> ranking(@PathVariable UUID peladaId) {
        return ResponseEntity.ok(service.rankingCompleto(peladaId));
    }

    @GetMapping("/artilheiros")
    public ResponseEntity<List<RankingJogadorDTO>> artilheiros(@PathVariable UUID peladaId) {
        return ResponseEntity.ok(service.artilheiros(peladaId));
    }

    @GetMapping("/assistencias")
    public ResponseEntity<List<RankingJogadorDTO>> assistencias(@PathVariable UUID peladaId) {
        return ResponseEntity.ok(service.assistencias(peladaId));
    }

    @GetMapping("/cartoes")
    public ResponseEntity<List<RankingJogadorDTO>> cartoes(@PathVariable UUID peladaId) {
        return ResponseEntity.ok(service.cartoes(peladaId));
    }
}
