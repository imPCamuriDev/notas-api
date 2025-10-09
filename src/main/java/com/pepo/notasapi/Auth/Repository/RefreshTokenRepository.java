package com.pepo.notasapi.Auth.Repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pepo.notasapi.Auth.Entity.RefreshToken;
import com.pepo.notasapi.Usuarios.Usuario;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsuarioAndRevokedFalse(Usuario usuario);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.usuario = :usuario")
    void revokeAllByUsuario(Usuario usuario);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteByExpiryDateBefore(Instant now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    void revokeByToken(String token);
}