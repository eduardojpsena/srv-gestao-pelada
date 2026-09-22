package br.com.gestao_pelada.auth.service;

import br.com.gestao_pelada.auth.model.dto.ConcluirRegistroRequestDTO;
import br.com.gestao_pelada.auth.model.dto.LoginRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RefreshRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RegisterRequestDTO;
import br.com.gestao_pelada.auth.model.dto.TokenResponseDTO;
import br.com.gestao_pelada.auth.model.dto.UsuarioResponseDTO;
import br.com.gestao_pelada.auth.model.entity.Usuario;
import br.com.gestao_pelada.auth.repository.UsuarioRepository;
import br.com.gestao_pelada.jogador.model.entity.Jogador;
import br.com.gestao_pelada.jogador.repository.JogadorRepository;
import br.com.gestao_pelada.shared.exception.AuthenticationFailedException;
import br.com.gestao_pelada.shared.exception.BusinessException;
import br.com.gestao_pelada.shared.security.JwtService;
import br.com.gestao_pelada.shared.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final JogadorRepository jogadorRepository;

    @Transactional
    public UsuarioResponseDTO registrar(RegisterRequestDTO request) {
        String email = normalizarEmail(request.email());
        Usuario existente = usuarioRepository.findByEmail(email).orElse(null);
        if (existente != null) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(email)
                .senhaHash(passwordEncoder.encode(request.senha()))
                .ativo(true)
                .cadastroConcluido(true)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        Jogador jogador = jogadorRepository.findByEmailIgnoreCase(email)
                .filter(item -> item.getUsuarioId() == null)
                .orElseGet(() -> Jogador.builder()
                        .nome(request.nome())
                        .email(email)
                        .ativo(true)
                        .build());
        jogador.setUsuarioId(salvo.getId());
        jogadorRepository.save(jogador);
        return toResponse(salvo);
    }

    @Transactional
    public UsuarioResponseDTO concluirRegistro(ConcluirRegistroRequestDTO request) {
        String email = normalizarEmail(request.email());
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationFailedException("E-mail ou senha inicial invalidos"));
        if (usuario.isCadastroConcluido()) {
            throw new BusinessException("O cadastro deste usuario ja esta concluido");
        }
        if (!passwordEncoder.matches(request.senhaInicial(), usuario.getSenhaHash())) {
            throw new AuthenticationFailedException("E-mail ou senha inicial invalidos");
        }
        usuario.setSenhaHash(passwordEncoder.encode(request.novaSenha()));
        usuario.setCadastroConcluido(true);
        return toResponse(usuarioRepository.save(usuario));
    }

    public TokenResponseDTO login(LoginRequestDTO request) {
        String email = normalizarEmail(request.email());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.senha()));
        } catch (BadCredentialsException ex) {
            throw new AuthenticationFailedException("E-mail ou senha invalidos");
        }

        SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(email);
        if (!userDetails.getUsuario().isCadastroConcluido()) {
            throw new AuthenticationFailedException(
                    "Cadastro pendente. Conclua o registro com uma nova senha antes de acessar");
        }
        return gerarTokens(userDetails);
    }

    public TokenResponseDTO refresh(RefreshRequestDTO request) {
        String token = request.refreshToken();
        String username = jwtService.extractUsername(token);
        if (username == null || !jwtService.isRefreshToken(token)) {
            throw new AuthenticationFailedException("Refresh token invalido");
        }

        SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new AuthenticationFailedException("Refresh token expirado ou invalido");
        }

        return gerarTokens(userDetails);
    }

    private TokenResponseDTO gerarTokens(SecurityUser userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new TokenResponseDTO(accessToken, refreshToken, "Bearer", jwtService.getAccessTokenExpirationMs());
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAtivo(),
                usuario.isCadastroConcluido());
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

