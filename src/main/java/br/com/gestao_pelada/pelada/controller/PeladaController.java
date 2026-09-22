package br.com.gestao_pelada.pelada.controller;

import br.com.gestao_pelada.pelada.service.PeladaService;
import br.com.gestao_pelada.pelada.model.dto.AlterarPapelRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.DecisaoSolicitacaoRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.MembroRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.MembroResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaJogadorRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaJogadorResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.PeladaResponseDTO;
import br.com.gestao_pelada.pelada.model.dto.ProvisionarMembroRequestDTO;
import br.com.gestao_pelada.pelada.model.dto.SolicitacaoEntradaResponseDTO;
import br.com.gestao_pelada.shared.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PeladaController implements PeladaApi {

    private final PeladaService service;

    @Override
    public ResponseEntity<PeladaResponseDTO> criar(PeladaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @Override
    public ResponseEntity<PeladaResponseDTO> buscarPorId(Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    public ResponseEntity<PageResponse<PeladaResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(service.listar(pageable)));
    }

    @Override
    public ResponseEntity<PeladaResponseDTO> atualizar(Long id, PeladaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PeladaJogadorResponseDTO> adicionarJogador(Long id, PeladaJogadorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarJogador(id, request));
    }

    @Override
    public ResponseEntity<List<PeladaJogadorResponseDTO>> listarJogadores(Long id) {
        return ResponseEntity.ok(service.listarJogadores(id));
    }

    @Override
    public ResponseEntity<Void> removerJogador(Long id, Long jogadorId) {
        service.removerJogador(id, jogadorId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MembroResponseDTO> adicionarMembro(Long id, MembroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarMembro(id, request));
    }

    @Override
    public ResponseEntity<MembroResponseDTO> provisionarMembro(Long id, ProvisionarMembroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.provisionarMembro(id, request));
    }

    @Override
    public ResponseEntity<SolicitacaoEntradaResponseDTO> solicitarEntrada(Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.solicitarEntrada(id));
    }

    @Override
    public ResponseEntity<List<SolicitacaoEntradaResponseDTO>> listarSolicitacoes(Long id) {
        return ResponseEntity.ok(service.listarSolicitacoes(id));
    }

    @Override
    public ResponseEntity<SolicitacaoEntradaResponseDTO> decidirSolicitacao(
            Long id, Long solicitacaoId, DecisaoSolicitacaoRequestDTO request) {
        return ResponseEntity.ok(service.decidirSolicitacao(id, solicitacaoId, request));
    }

    @Override
    public ResponseEntity<MembroResponseDTO> alterarPapel(
            Long id, Long usuarioId, AlterarPapelRequestDTO request) {
        return ResponseEntity.ok(service.alterarPapel(id, usuarioId, request));
    }
}

