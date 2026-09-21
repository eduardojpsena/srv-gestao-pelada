package br.com.gestao_pelada.auth.application;

import br.com.gestao_pelada.auth.application.dto.LoginRequest;
import br.com.gestao_pelada.auth.application.dto.RefreshRequest;
import br.com.gestao_pelada.auth.application.dto.RegisterRequest;
import br.com.gestao_pelada.auth.application.dto.TokenResponse;
import br.com.gestao_pelada.auth.application.dto.UsuarioResponseDTO;
import br.com.gestao_pelada.auth.domain.Usuario;
import br.com.gestao_pelada.auth.infrastructure.UsuarioRepository;
import br.com.gestao_pelada.shared.enums.Role;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public UsuarioResponseDTO registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ja existe um usuario cadastrado com este e-mail");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senhaHash(passwordEncoder.encode(request.senha()))
                .role(request.role() != null ? request.role() : Role.JOGADOR)
                .ativo(true)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponse(salvo);
    }

    public TokenResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        } catch (BadCredentialsException ex) {
            throw new AuthenticationFailedException("E-mail ou senha invalidos");
        }

        SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(request.email());
        return gerarTokens(userDetails);
    }

    public TokenResponse refresh(RefreshRequest request) {
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

    private TokenResponse gerarTokens(SecurityUser userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new TokenResponse(accessToken, refreshToken, "Bearer", jwtService.getAccessTokenExpirationMs());
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole(), usuario.isAtivo());
    }
}
