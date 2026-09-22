package br.com.gestao_pelada.auth.controller;

import br.com.gestao_pelada.auth.model.dto.ConcluirRegistroRequestDTO;
import br.com.gestao_pelada.auth.model.dto.LoginRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RefreshRequestDTO;
import br.com.gestao_pelada.auth.model.dto.RegisterRequestDTO;
import br.com.gestao_pelada.auth.model.dto.TokenResponseDTO;
import br.com.gestao_pelada.auth.model.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Autenticacao", description = "Login, registro e renovacao de tokens JWT")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(
            summary = "Registra um novo usuario",
            description =
                    """
                    Cria um usuario sem papel global. Os papeis sao atribuidos em cada pelada.
                    Se o e-mail foi provisionado por um administrador, conclui o cadastro e substitui a senha inicial.
                    """)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Usuario registrado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "409", description = "E-mail ja cadastrado")
            })
    @PostMapping("/register")
    ResponseEntity<UsuarioResponseDTO> registrar(@RequestBody @Valid RegisterRequestDTO request);

    @Operation(
            summary = "Conclui cadastro provisionado",
            description = "Valida a senha inicial definida pelo ADMIN e exige a definicao de uma nova senha.")
    @PostMapping("/complete-registration")
    ResponseEntity<UsuarioResponseDTO> concluirRegistro(@RequestBody @Valid ConcluirRegistroRequestDTO request);

    @Operation(
            summary = "Autentica um usuario",
            description = "Valida as credenciais informadas e retorna o par de tokens de acesso e renovacao.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
                @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
            })
    @PostMapping("/login")
    ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request);

    @Operation(
            summary = "Renova o token de acesso",
            description = "Gera um novo par de tokens a partir de um refresh token valido.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
                @ApiResponse(responseCode = "401", description = "Refresh token invalido ou expirado")
            })
    @PostMapping("/refresh")
    ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO request);
}

