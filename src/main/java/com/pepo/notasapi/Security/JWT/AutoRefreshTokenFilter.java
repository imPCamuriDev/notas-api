package com.pepo.notasapi.Security.JWT;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepo.notasapi.Auth.DTO.AuthResponse;
import com.pepo.notasapi.Auth.Entity.RefreshToken;
import com.pepo.notasapi.Auth.Service.RefreshTokenService;
import com.pepo.notasapi.Usuarios.Usuario;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro que renova automaticamente o JWT quando ele expira
 * Se o JWT estiver expirado MAS o refresh token for válido,
 * gera novos tokens e os retorna no header da resposta
 */
@Component
@Order(1) // Executa ANTES do JwtAuthenticationFilter
public class AutoRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final ActiveJwtService activeJwtService;
    private final ObjectMapper objectMapper;

    public AutoRefreshTokenFilter(JwtUtil jwtUtil,
                                  RefreshTokenService refreshTokenService,
                                  TokenBlacklistService tokenBlacklistService,
                                  ActiveJwtService activeJwtService,
                                  ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.activeJwtService = activeJwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String refreshTokenHeader = request.getHeader("X-Refresh-Token");

        // Se não tiver ambos os headers, continua normalmente
        if (authHeader == null || !authHeader.startsWith("Bearer ") || refreshTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String refreshTokenStr = refreshTokenHeader;

        // Verifica se está na blacklist (logout explícito)
        if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Tenta validar o JWT
            jwtUtil.extractUsername(jwt);
            // Se chegou aqui, JWT é válido, continua normalmente
            filterChain.doFilter(request, response);
            return;

        } catch (ExpiredJwtException e) {
            // JWT EXPIRADO - Tenta renovar automaticamente
            logger.info("JWT expirado detectado, tentando renovar automaticamente...");

            try {
                // Busca e valida o refresh token
                RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr);
                refreshToken = refreshTokenService.verifyExpiration(refreshToken);

                Usuario usuario = refreshToken.getUsuario();

                // Gera novo JWT
                String newJwt = jwtUtil.generateToken(
                        usuario.getEmail().getValue(),
                        usuario.getId()
                );

                // Registra o novo token ativo
                activeJwtService.registerActiveToken(usuario.getId(), newJwt);

                // Gera novo refresh token
                RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(usuario.getId());

                // Adiciona novos tokens nos headers da resposta
                response.setHeader("X-New-Access-Token", newJwt);
                response.setHeader("X-New-Refresh-Token", newRefreshToken.getToken());
                response.setHeader("X-Token-Refreshed", "true");

                // Cria uma requisição wrapper com o novo token
                HttpServletRequest wrappedRequest = new HeaderModifierHttpServletRequest(
                        request, 
                        "Bearer " + newJwt
                );

                logger.info("Token renovado automaticamente para usuário: " + usuario.getEmail().getValue());

                // Continua com o novo token
                filterChain.doFilter(wrappedRequest, response);
                return;

            } catch (Exception refreshError) {
                // Falha ao renovar - refresh token inválido ou expirado
                logger.error("Falha ao renovar token automaticamente: " + refreshError.getMessage());
                
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(
                    new ErrorResponse(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized",
                        "Sessão expirada. Por favor, faça login novamente.",
                        request.getRequestURI()
                    )
                ));
                return;
            }

        } catch (Exception e) {
            // Outros erros no JWT
            logger.error("Erro ao processar JWT: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    // Classe interna para resposta de erro
    private static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(int status, String error, String message, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
    }
}