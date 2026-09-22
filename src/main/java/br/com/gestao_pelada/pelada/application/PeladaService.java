package br.com.gestao_pelada.pelada.application;

import br.com.gestao_pelada.auth.domain.Usuario;
import br.com.gestao_pelada.auth.infrastructure.UsuarioRepository;
import br.com.gestao_pelada.jogador.domain.Jogador;
import br.com.gestao_pelada.jogador.infrastructure.JogadorRepository;
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
import br.com.gestao_pelada.pelada.application.mapper.PeladaMapper;
import br.com.gestao_pelada.pelada.domain.PapelPelada;
import br.com.gestao_pelada.pelada.domain.Pelada;
import br.com.gestao_pelada.pelada.domain.PeladaJogador;
import br.com.gestao_pelada.pelada.domain.PeladaMembro;
import br.com.gestao_pelada.pelada.domain.SolicitacaoEntrada;
import br.com.gestao_pelada.pelada.domain.StatusPelada;
import br.com.gestao_pelada.pelada.domain.StatusSolicitacaoEntrada;
import br.com.gestao_pelada.pelada.infrastructure.PeladaJogadorRepository;
import br.com.gestao_pelada.pelada.infrastructure.PeladaMembroRepository;
import br.com.gestao_pelada.pelada.infrastructure.PeladaRepository;
import br.com.gestao_pelada.pelada.infrastructure.SolicitacaoEntradaRepository;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.exception.ResourceNotFoundException;
import br.com.gestao_pelada.shared.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PeladaService {

    private final PeladaRepository peladaRepository;
    private final PeladaJogadorRepository peladaJogadorRepository;
    private final PeladaMembroRepository membroRepository;
    private final SolicitacaoEntradaRepository solicitacaoRepository;
    private final JogadorRepository jogadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PeladaMapper mapper;

    public PeladaResponseDTO criar(PeladaRequestDTO request) {
        Usuario usuario = usuarioAtual();
        exigirCadastroConcluido(usuario);
        Pelada entity = mapper.toEntity(request);
        entity.setOrganizadorId(usuario.getId());
        Pelada salva = peladaRepository.save(entity);
        membroRepository.save(PeladaMembro.builder()
                .peladaId(salva.getId())
                .usuarioId(usuario.getId())
                .papel(PapelPelada.ADMIN)
                .build());
        vincularJogador(salva.getId(), jogadorDoUsuario(usuario.getId()));
        return mapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public PeladaResponseDTO buscarPorId(UUID id) {
        exigirMembro(id);
        return mapper.toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<PeladaResponseDTO> listar(Pageable pageable) {
        Page<PeladaMembro> membros = membroRepository.findByUsuarioIdAndAtivoTrue(usuarioAtual().getId(), pageable);
        List<PeladaResponseDTO> conteudo = membros.stream()
                .map(item -> peladaRepository.findById(item.getPeladaId()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .map(mapper::toResponse)
                .toList();
        return new PageImpl<>(conteudo, pageable, membros.getTotalElements());
    }

    public PeladaResponseDTO atualizar(UUID id, PeladaRequestDTO request) {
        exigirGestor(id);
        Pelada entity = buscarEntidade(id);
        mapper.updateFromRequest(request, entity);
        return mapper.toResponse(peladaRepository.save(entity));
    }

    public void deletar(UUID id) {
        exigirAdmin(id);
        Pelada entity = buscarEntidade(id);
        entity.setStatus(StatusPelada.INATIVA);
        peladaRepository.save(entity);
    }

    public MembroResponseDTO adicionarMembro(UUID peladaId, MembroRequestDTO request) {
        exigirAdmin(peladaId);
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(request.email()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado para o e-mail informado"));
        exigirCadastroConcluido(usuario);
        return salvarMembro(peladaId, usuario, request.papel() == null ? PapelPelada.JOGADOR : request.papel());
    }

    public MembroResponseDTO provisionarMembro(UUID peladaId, ProvisionarMembroRequestDTO request) {
        exigirAdmin(peladaId);
        String email = normalizarEmail(request.email());
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nome(request.nome())
                .email(email)
                .senhaHash(passwordEncoder.encode(request.senhaPadrao()))
                .cadastroConcluido(false)
                .ativo(true)
                .build());
        jogadorRepository.save(Jogador.builder()
                .usuarioId(usuario.getId())
                .nome(request.nome())
                .email(email)
                .ativo(true)
                .build());
        MembroResponseDTO response = salvarMembro(peladaId, usuario, PapelPelada.JOGADOR);
        return response;
    }

    public SolicitacaoEntradaResponseDTO solicitarEntrada(UUID peladaId) {
        buscarEntidade(peladaId);
        Usuario usuario = usuarioAtual();
        exigirCadastroConcluido(usuario);
        if (membroRepository.existsByPeladaIdAndUsuarioIdAndAtivoTrue(peladaId, usuario.getId())) {
            throw new BusinessException("Usuario ja participa desta pelada");
        }
        if (solicitacaoRepository.existsByPeladaIdAndUsuarioIdAndStatus(
                peladaId, usuario.getId(), StatusSolicitacaoEntrada.PENDENTE)) {
            throw new BusinessException("Ja existe uma solicitacao pendente para esta pelada");
        }
        SolicitacaoEntrada solicitacao = solicitacaoRepository.save(SolicitacaoEntrada.builder()
                .peladaId(peladaId)
                .usuarioId(usuario.getId())
                .build());
        return toResponse(solicitacao, usuario);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoEntradaResponseDTO> listarSolicitacoes(UUID peladaId) {
        exigirAdmin(peladaId);
        return solicitacaoRepository.findByPeladaIdAndStatus(peladaId, StatusSolicitacaoEntrada.PENDENTE).stream()
                .map(item -> toResponse(item, buscarUsuario(item.getUsuarioId())))
                .toList();
    }

    public SolicitacaoEntradaResponseDTO decidirSolicitacao(
            UUID peladaId, UUID solicitacaoId, DecisaoSolicitacaoRequestDTO request) {
        exigirAdmin(peladaId);
        if (request.status() == StatusSolicitacaoEntrada.PENDENTE) {
            throw new BusinessException("A decisao deve ser APROVADA ou RECUSADA");
        }
        SolicitacaoEntrada solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .filter(item -> item.getPeladaId().equals(peladaId))
                .orElseThrow(() -> new ResourceNotFoundException("Solicitacao nao encontrada"));
        if (solicitacao.getStatus() != StatusSolicitacaoEntrada.PENDENTE) {
            throw new BusinessException("Solicitacao ja foi decidida");
        }
        Usuario usuario = buscarUsuario(solicitacao.getUsuarioId());
        solicitacao.setStatus(request.status());
        solicitacao.setDecididoEm(Instant.now());
        solicitacao.setDecididoPor(usuarioAtual().getId());
        if (request.status() == StatusSolicitacaoEntrada.APROVADA) {
            salvarMembro(peladaId, usuario, PapelPelada.JOGADOR);
        }
        return toResponse(solicitacaoRepository.save(solicitacao), usuario);
    }

    public MembroResponseDTO alterarPapel(UUID peladaId, UUID usuarioId, AlterarPapelRequestDTO request) {
        exigirAdmin(peladaId);
        PeladaMembro membro = membroRepository.findByPeladaIdAndUsuarioId(peladaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro nao encontrado"));
        Usuario usuario = buscarUsuario(usuarioId);
        exigirCadastroConcluido(usuario);
        if (membro.getPapel() == PapelPelada.ADMIN
                && request.papel() != PapelPelada.ADMIN
                && membroRepository.countByPeladaIdAndPapelAndAtivoTrue(peladaId, PapelPelada.ADMIN) == 1) {
            throw new BusinessException("A pelada deve possuir ao menos um administrador");
        }
        membro.setPapel(request.papel());
        return toResponse(membroRepository.save(membro), usuario);
    }

    public PeladaJogadorResponseDTO adicionarJogador(UUID peladaId, PeladaJogadorRequestDTO request) {
        exigirGestor(peladaId);
        Jogador jogador = jogadorRepository.findById(request.jogadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao encontrado: " + request.jogadorId()));
        if (jogador.getUsuarioId() == null
                || !membroRepository.existsByPeladaIdAndUsuarioIdAndAtivoTrue(peladaId, jogador.getUsuarioId())) {
            throw new BusinessException("O jogador deve ser membro registrado desta pelada");
        }
        PeladaJogador vinculo = vincularJogador(peladaId, jogador);
        vinculo.setNotaPelada(request.notaPelada());
        vinculo.setMensalista(Boolean.TRUE.equals(request.mensalista()));
        return toResponse(peladaJogadorRepository.save(vinculo), jogador);
    }

    @Transactional(readOnly = true)
    public List<PeladaJogadorResponseDTO> listarJogadores(UUID peladaId) {
        exigirMembro(peladaId);
        return peladaJogadorRepository.findByPeladaIdAndAtivoTrue(peladaId).stream()
                .map(vinculo -> toResponse(vinculo, jogadorRepository.findById(vinculo.getJogadorId()).orElse(null)))
                .toList();
    }

    public void removerJogador(UUID peladaId, UUID jogadorId) {
        exigirAdmin(peladaId);
        PeladaJogador vinculo = peladaJogadorRepository.findByPeladaIdAndJogadorId(peladaId, jogadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador nao vinculado a esta pelada"));
        vinculo.setAtivo(false);
        peladaJogadorRepository.save(vinculo);
    }

    Pelada buscarEntidade(UUID id) {
        return peladaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pelada nao encontrada: " + id));
    }

    private MembroResponseDTO salvarMembro(UUID peladaId, Usuario usuario, PapelPelada papel) {
        buscarEntidade(peladaId);
        PeladaMembro membro = membroRepository.findByPeladaIdAndUsuarioId(peladaId, usuario.getId()).orElse(null);
        if (membro != null && membro.isAtivo()) {
            throw new BusinessException("Usuario ja participa desta pelada");
        }
        if (membro == null) {
            membro = PeladaMembro.builder()
                        .peladaId(peladaId)
                        .usuarioId(usuario.getId())
                        .build();
        }
        membro.setPapel(papel);
        membro.setAtivo(true);
        PeladaMembro salvo = membroRepository.save(membro);
        vincularJogador(peladaId, jogadorDoUsuario(usuario.getId()));
        return toResponse(salvo, usuario);
    }

    private PeladaJogador vincularJogador(UUID peladaId, Jogador jogador) {
        PeladaJogador vinculo = peladaJogadorRepository.findByPeladaIdAndJogadorId(peladaId, jogador.getId())
                .orElseGet(() -> PeladaJogador.builder()
                        .peladaId(peladaId)
                        .jogadorId(jogador.getId())
                        .build());
        vinculo.setAtivo(true);
        return peladaJogadorRepository.save(vinculo);
    }

    private void exigirMembro(UUID peladaId) {
        buscarEntidade(peladaId);
        if (!membroRepository.existsByPeladaIdAndUsuarioIdAndAtivoTrue(peladaId, usuarioAtual().getId())) {
            throw new AccessDeniedException("Usuario nao participa desta pelada");
        }
    }

    private void exigirGestor(UUID peladaId) {
        PapelPelada papel = papelAtual(peladaId);
        if (papel != PapelPelada.ADMIN && papel != PapelPelada.ORGANIZADOR) {
            throw new AccessDeniedException("Operacao permitida apenas para gestores da pelada");
        }
    }

    private void exigirAdmin(UUID peladaId) {
        if (papelAtual(peladaId) != PapelPelada.ADMIN) {
            throw new AccessDeniedException("Operacao permitida apenas para administradores da pelada");
        }
    }

    private PapelPelada papelAtual(UUID peladaId) {
        return membroRepository.findByPeladaIdAndUsuarioId(peladaId, usuarioAtual().getId())
                .filter(PeladaMembro::isAtivo)
                .map(PeladaMembro::getPapel)
                .orElseThrow(() -> new AccessDeniedException("Usuario nao participa desta pelada"));
    }

    private Usuario usuarioAtual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SecurityUser securityUser)) {
            throw new AccessDeniedException("Usuario nao autenticado");
        }
        return securityUser.getUsuario();
    }

    private Usuario buscarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    }

    private Jogador jogadorDoUsuario(UUID usuarioId) {
        return jogadorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new BusinessException("Perfil de jogador nao encontrado"));
    }

    private void exigirCadastroConcluido(Usuario usuario) {
        if (!usuario.isCadastroConcluido()) {
            throw new BusinessException("Conclua o registro e altere a senha inicial antes de continuar");
        }
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private MembroResponseDTO toResponse(PeladaMembro membro, Usuario usuario) {
        return new MembroResponseDTO(
                membro.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                membro.getPapel(),
                usuario.isCadastroConcluido(),
                membro.isAtivo());
    }

    private SolicitacaoEntradaResponseDTO toResponse(SolicitacaoEntrada solicitacao, Usuario usuario) {
        return new SolicitacaoEntradaResponseDTO(
                solicitacao.getId(),
                solicitacao.getPeladaId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                solicitacao.getStatus(),
                solicitacao.getCriadoEm());
    }

    private PeladaJogadorResponseDTO toResponse(PeladaJogador vinculo, Jogador jogador) {
        return new PeladaJogadorResponseDTO(
                vinculo.getId(),
                vinculo.getPeladaId(),
                vinculo.getJogadorId(),
                jogador != null ? jogador.getNome() : null,
                vinculo.getNotaPelada(),
                vinculo.isMensalista(),
                vinculo.isAtivo());
    }
}
