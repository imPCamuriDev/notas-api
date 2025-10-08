package com.pepo.notasapi.Auth.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepo.notasapi.Auth.DTO.AuthResponse;
import com.pepo.notasapi.Auth.DTO.LoginRequest;
import com.pepo.notasapi.Auth.DTO.RegisterRequest;
import com.pepo.notasapi.Auth.Service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Login", description = "Realiza login e retorna token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registro", description = "Registra novo usuário e retorna token JWT")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}