package br.com.gestao_pelada.partida.controller;

import br.com.gestao_pelada.partida.service.PartidaService;
import br.com.gestao_pelada.partida.model.dto.ParticipanteRequestDTO;
import br.com.gestao_pelada.partida.model.dto.ParticipanteResponseDTO;
import br.com.gestao_pelada.partida.model.dto.PresencaUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipanteController implements ParticipanteApi {

    private final PartidaService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<ParticipanteResponseDTO> adicionar(Long partidaId, ParticipanteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarParticipante(partidaId, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessarPartida(#partidaId, authentication)")
    public ResponseEntity<List<ParticipanteResponseDTO>> listar(Long partidaId) {
        return ResponseEntity.ok(service.listarParticipantes(partidaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<ParticipanteResponseDTO> atualizarPresenca(
            Long partidaId, Long jogadorId, PresencaUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarPresenca(partidaId, jogadorId, request.presente()));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<Void> remover(Long partidaId, Long jogadorId) {
        service.removerParticipante(partidaId, jogadorId);
        return ResponseEntity.noContent().build();
    }
}

