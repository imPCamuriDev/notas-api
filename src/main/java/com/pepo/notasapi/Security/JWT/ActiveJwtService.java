package com.pepo.notasapi.Security.JWT;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Serviço para rastrear JWTs ativos por usuário
 * Invalida automaticamente tokens antigos quando um novo login acontece
 */
@Service
public class ActiveJwtService {

    // Map: userId -> Set de tokens ativos
    private final Map<Long, String> activeTokensByUser = new ConcurrentHashMap<>();
    
    private final JwtUtil jwtUtil;

    public ActiveJwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra um novo token ativo para o usuário
     * Remove automaticamente o token anterior (se existir)
     */
    public void registerActiveToken(Long userId, String token) {
        activeTokensByUser.put(userId, token);
    }

    /**
     * Verifica se o token está ativo para o usuário
     */
    public boolean isTokenActive(Long userId, String token) {
        String activeToken = activeTokensByUser.get(userId);
        return token.equals(activeToken);
    }

    /**
     * Remove token ativo do usuário
     */
    public void removeActiveToken(Long userId) {
        activeTokensByUser.remove(userId);
    }

    /**
     * Remove todos os tokens ativos do usuário
     */
    public void removeAllUserTokens(Long userId) {
        activeTokensByUser.remove(userId);
    }

    /**
     * Limpa tokens expirados do cache a cada hora
     */
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void cleanupExpiredTokens() {
        activeTokensByUser.entrySet().removeIf(entry -> {
            try {
                String token = entry.getValue();
                Date expiration = jwtUtil.extractExpiration(token);
                return expiration.before(new Date());
            } catch (Exception e) {
                // Se não conseguir verificar, remove do cache
                return true;
            }
        });
    }

    /**
     * Retorna o número de usuários com tokens ativos
     */
    public int getActiveUsersCount() {
        return activeTokensByUser.size();
    }
}