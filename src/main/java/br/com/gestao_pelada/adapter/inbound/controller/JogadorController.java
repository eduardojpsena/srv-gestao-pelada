package br.com.gestao_pelada.adapter.inbound.controller;

import br.com.gestao_pelada.adapter.inbound.models.JogadorRequestDTO;
import br.com.gestao_pelada.adapter.inbound.models.JogadorResponseDTO;
import br.com.gestao_pelada.application.port.inbound.JogadorUseCase;
import br.com.gestao_pelada.domain.model.Jogador;
import br.com.gestao_pelada.mapper.JogadorMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorUseCase useCase;
    private final JogadorMapper mapper;

    @PostMapping()
    public ResponseEntity<JogadorResponseDTO> criar(@Valid @RequestBody JogadorRequestDTO request) {
        Jogador jogadorRequest = mapper.dtoToDomain(request);
        JogadorResponseDTO newJogador = mapper
                .domainToResponseDTO(useCase.criarJogador(jogadorRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(newJogador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogadorResponseDTO> buscarPorId(@PathVariable Long id) {
        JogadorResponseDTO jogador = mapper
                .domainToResponseDTO(useCase.buscarJogador(id));
        return ResponseEntity.ok(jogador);
    }

    @GetMapping()
    public ResponseEntity<List<JogadorResponseDTO>> listar() {
        List<JogadorResponseDTO> jogadores = useCase
                .listarJogadores()
                .stream()
                .map(mapper::domainToResponseDTO)
                .toList();
        return ResponseEntity.ok(jogadores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        useCase.deletarJogador(id);
        return ResponseEntity.noContent().build();
    }
}
