package br.com.gestao_pelada.adapter.inbound.controller;

import br.com.gestao_pelada.adapter.inbound.models.JogadorRequestDTO;
import br.com.gestao_pelada.application.port.inbound.JogadorUseCase;
import br.com.gestao_pelada.domain.model.Jogador;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorUseCase jogadorUseCase;

    @PostMapping()
    public ResponseEntity<Jogador> criar(@Valid @RequestBody JogadorRequestDTO request) {
        Jogador jogador = jogadorUseCase.criarJogador(request);
        return new ResponseEntity<>(jogador, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogador> buscarPorId(@PathVariable Long id) {
        Jogador jogador = jogadorUseCase.buscarJogador(id);
        return new ResponseEntity<>(jogador, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Jogador>> listar() {
        List<Jogador> jogadores = jogadorUseCase.listarJogadores();
        return new ResponseEntity<>(jogadores, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        jogadorUseCase.deletarJogador(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
