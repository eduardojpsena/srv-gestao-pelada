package br.com.gestao_pelada.auth.controller;

import br.com.gestao_pelada.auth.service.AuthService;
import br.com.gestao_pelada.auth.model.dto.ConcluirRegistroRequestDTO;
import br.com.gestao_pelada.auth.model.dto.LoginRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RefreshRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RegisterRequestDTO;
import br.com.gestao_pelada.auth.model.dto.TokenResponseDTO;
import br.com.gestao_pelada.auth.model.dto.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<UsuarioResponseDTO> registrar(RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> concluirRegistro(ConcluirRegistroRequestDTO request) {
        return ResponseEntity.ok(authService.concluirRegistro(request));
    }

    @Override
    public ResponseEntity<TokenResponseDTO> login(LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Override
    public ResponseEntity<TokenResponseDTO> refresh(RefreshRequestDTO request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}

