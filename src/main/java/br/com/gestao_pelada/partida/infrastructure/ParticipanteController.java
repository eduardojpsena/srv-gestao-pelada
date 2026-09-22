package br.com.gestao_pelada.partida.infrastructure;

import br.com.gestao_pelada.partida.application.PartidaService;
import br.com.gestao_pelada.partida.application.dto.ParticipanteRequestDTO;
import br.com.gestao_pelada.partida.application.dto.ParticipanteResponseDTO;
import br.com.gestao_pelada.partida.application.dto.PresencaUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ParticipanteController implements ParticipanteApi {

    private final PartidaService service;

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<ParticipanteResponseDTO> adicionar(UUID partidaId, ParticipanteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarParticipante(partidaId, request));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeAcessarPartida(#partidaId, authentication)")
    public ResponseEntity<List<ParticipanteResponseDTO>> listar(UUID partidaId) {
        return ResponseEntity.ok(service.listarParticipantes(partidaId));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<ParticipanteResponseDTO> atualizarPresenca(
            UUID partidaId, UUID jogadorId, PresencaUpdateDTO request) {
        return ResponseEntity.ok(service.atualizarPresenca(partidaId, jogadorId, request.presente()));
    }

    @Override
    @PreAuthorize("@peladaAuthorization.podeGerenciarPartida(#partidaId, authentication)")
    public ResponseEntity<Void> remover(UUID partidaId, UUID jogadorId) {
        service.removerParticipante(partidaId, jogadorId);
        return ResponseEntity.noContent().build();
    }
}
