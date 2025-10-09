package com.pepo.notasapi.Auth.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.pepo.notasapi.Auth.DTO.AuthResponse;
import com.pepo.notasapi.Auth.DTO.LoginRequest;
import com.pepo.notasapi.Auth.DTO.RefreshTokenRequest;
import com.pepo.notasapi.Auth.DTO.RegisterRequest;
import com.pepo.notasapi.Auth.Service.AuthService;
import com.pepo.notasapi.Exceptions.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza login e retorna token JWT e refresh token")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            WebRequest request
    ) {
        try {
            AuthResponse response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao realizar login: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registro", description = "Registra novo usuário e retorna token JWT e refresh token")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest,
            WebRequest request
    ) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    "Conflict",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao registrar usuário: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Renova o token JWT usando refresh token")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest,
            WebRequest request
    ) {
        try {
            AuthResponse response = authService.refreshToken(refreshTokenRequest.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao renovar token: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Invalida o token JWT atual e todos os refresh tokens do usuário ( envie: Bearer {token} )",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authHeader,
            WebRequest request
    ) {
        try {
            authService.logout(authHeader);
            return ResponseEntity.ok("Logout realizado com sucesso");
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao realizar logout: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }
}