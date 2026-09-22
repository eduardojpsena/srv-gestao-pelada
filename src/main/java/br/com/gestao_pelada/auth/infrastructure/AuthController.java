package br.com.gestao_pelada.auth.infrastructure;

import br.com.gestao_pelada.auth.application.AuthService;
import br.com.gestao_pelada.auth.application.dto.ConcluirRegistroRequest;
import br.com.gestao_pelada.auth.application.dto.LoginRequest;
import br.com.gestao_pelada.auth.application.dto.RefreshRequest;
import br.com.gestao_pelada.auth.application.dto.RegisterRequest;
import br.com.gestao_pelada.auth.application.dto.TokenResponse;
import br.com.gestao_pelada.auth.application.dto.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<UsuarioResponseDTO> registrar(RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> concluirRegistro(ConcluirRegistroRequest request) {
        return ResponseEntity.ok(authService.concluirRegistro(request));
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Override
    public ResponseEntity<TokenResponse> refresh(RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
