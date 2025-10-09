package com.pepo.notasapi.Auth.Service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pepo.notasapi.Auth.Entity.RefreshToken;
import com.pepo.notasapi.Auth.Repository.RefreshTokenRepository;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UsuarioRepository usuarioRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // IMPORTANTE: Revoga todos os refresh tokens anteriores do usuário
        // Isso garante que apenas o login mais recente seja válido
        refreshTokenRepository.revokeAllByUsuario(usuario);

        // Cria novo refresh token
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusMillis(refreshTokenExpiration);

        RefreshToken refreshToken = new RefreshToken(usuario, token, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        refreshTokenRepository.revokeAllByUsuario(usuario);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Por favor, faça login novamente");
        }
        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token foi revogado. Por favor, faça login novamente");
        }
        return token;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token não encontrado"));
    }

    @Transactional
    public void revokeByToken(String token) {
        refreshTokenRepository.revokeByToken(token);
    }

    @Transactional
    public void revokeByUsuario(Usuario usuario) {
        refreshTokenRepository.revokeAllByUsuario(usuario);
    }

    // Limpa tokens expirados todos os dias à meia-noite
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}