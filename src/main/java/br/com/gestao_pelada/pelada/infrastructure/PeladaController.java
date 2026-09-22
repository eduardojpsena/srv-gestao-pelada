package br.com.gestao_pelada.pelada.infrastructure;

import br.com.gestao_pelada.pelada.application.PeladaService;
import br.com.gestao_pelada.pelada.application.dto.AlterarPapelRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.DecisaoSolicitacaoRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.MembroRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.MembroResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.PeladaResponseDTO;
import br.com.gestao_pelada.pelada.application.dto.ProvisionarMembroRequestDTO;
import br.com.gestao_pelada.pelada.application.dto.SolicitacaoEntradaResponseDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PeladaController implements PeladaApi {

    private final PeladaService service;

    @Override
    public ResponseEntity<PeladaResponseDTO> criar(PeladaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @Override
    public ResponseEntity<PeladaResponseDTO> buscarPorId(UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PageResponse<PeladaResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @Override
    public ResponseEntity<PeladaResponseDTO> atualizar(UUID id, PeladaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    public ResponseEntity<Void> deletar(UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PeladaJogadorResponseDTO> adicionarJogador(UUID id, PeladaJogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarJogador(id, request));
    }

    @Override
    public ResponseEntity<List<PeladaJogadorResponseDTO>> listarJogadores(UUID id) {
        return ResponseEntity.ok(service.listarJogadores(id));
    }

    @Override
    public ResponseEntity<Void> removerJogador(UUID id, UUID jogadorId) {
        service.removerJogador(id, jogadorId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MembroResponseDTO> adicionarMembro(UUID id, MembroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarMembro(id, request));
    }

    @Override
    public ResponseEntity<MembroResponseDTO> provisionarMembro(UUID id, ProvisionarMembroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.provisionarMembro(id, request));
    }

    @Override
    public ResponseEntity<SolicitacaoEntradaResponseDTO> solicitarEntrada(UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.solicitarEntrada(id));
    }

    @Override
    public ResponseEntity<List<SolicitacaoEntradaResponseDTO>> listarSolicitacoes(UUID id) {
        return ResponseEntity.ok(service.listarSolicitacoes(id));
    }

    @Override
    public ResponseEntity<SolicitacaoEntradaResponseDTO> decidirSolicitacao(
            UUID id, UUID solicitacaoId, DecisaoSolicitacaoRequestDTO request) {
        return ResponseEntity.ok(service.decidirSolicitacao(id, solicitacaoId, request));
    }

    @Override
    public ResponseEntity<MembroResponseDTO> alterarPapel(
            UUID id, UUID usuarioId, AlterarPapelRequestDTO request) {
        return ResponseEntity.ok(service.alterarPapel(id, usuarioId, request));
    }
}
