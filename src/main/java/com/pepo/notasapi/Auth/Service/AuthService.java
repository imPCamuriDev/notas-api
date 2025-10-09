package com.pepo.notasapi.Auth.Service;

import com.pepo.notasapi.Security.JWT.ActiveJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pepo.notasapi.Auth.DTO.AuthResponse;
import com.pepo.notasapi.Auth.DTO.LoginRequest;
import com.pepo.notasapi.Auth.DTO.RegisterRequest;
import com.pepo.notasapi.Auth.Entity.RefreshToken;
import com.pepo.notasapi.Security.CustomUserDetailsService;
import com.pepo.notasapi.Security.JWT.JwtUtil;
import com.pepo.notasapi.Security.JWT.TokenBlacklistService;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final ActiveJwtService activeJwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       CustomUserDetailsService userDetailsService,
                       UsuarioRepository usuarioRepository,
                       TokenBlacklistService tokenBlacklistService,
                       RefreshTokenService refreshTokenService,
                       ActiveJwtService activeJwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
        this.activeJwtService = activeJwtService;
    }

    @Transactional
    public AuthResponse authenticate(LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                throw new RuntimeException("Email é obrigatório");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                throw new RuntimeException("Senha é obrigatória");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            Usuario usuario = userDetailsService.loadUserEntityByEmail(loginRequest.getEmail());

            String token = jwtUtil.generateToken(
                    usuario.getEmail().getValue(),
                    usuario.getId()
            );

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());

            return new AuthResponse(
                    token,
                    refreshToken.getToken(),
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail().getValue()
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciais inválidas - Email ou senha incorretos");
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        try {
            // Validações básicas
            if (registerRequest.getNome() == null || registerRequest.getNome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome é obrigatório");
            }

            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email é obrigatório");
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Senha é obrigatória");
            }

            // Verificar se usuário já existe
            EmailVO emailVO = new EmailVO(registerRequest.getEmail());
            if (usuarioRepository.findByEmail(emailVO).isPresent()) {
                throw new RuntimeException("Email já cadastrado");
            }

            // Criar novo usuário
            Usuario usuario = new Usuario();
            usuario.setNome(registerRequest.getNome());
            usuario.setEmail(emailVO);
            usuario.setPassword(new PasswordHashVO(registerRequest.getPassword()));
            usuario.setEmailVerificado(false);

            usuario = usuarioRepository.save(usuario);

            // Gerar token JWT
            String token = jwtUtil.generateToken(
                    usuario.getEmail().getValue(),
                    usuario.getId()
            );

            // Gerar refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());

            return new AuthResponse(
                    token,
                    refreshToken.getToken(),
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail().getValue()
            );
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void logout(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            throw new IllegalArgumentException("Token de autorização não fornecido");
        }

        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Formato de token inválido. Use: Bearer {token}");
        }

        String token = authHeader.substring(7);

        if (token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token vazio");
        }

        // Adiciona JWT à blacklist
        tokenBlacklistService.blacklistToken(token);

        // Extrai email do token e revoga todos os refresh tokens do usuário
        try {
            String email = jwtUtil.extractUsername(token);
            Usuario usuario = userDetailsService.loadUserEntityByEmail(email);
            refreshTokenService.revokeByUsuario(usuario);
        } catch (Exception e) {
            // Se não conseguir extrair usuário do token, apenas adiciona à blacklist
        }
    }

    @Transactional
    public AuthResponse refreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr);
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        Usuario usuario = refreshToken.getUsuario();

        // Gera novo JWT
        String newJwtToken = jwtUtil.generateToken(
                usuario.getEmail().getValue(),
                usuario.getId()
        );

        // Gera novo refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(usuario.getId());

        return new AuthResponse(
                newJwtToken,
                newRefreshToken.getToken(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail().getValue()
        );
    }
}