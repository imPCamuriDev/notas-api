package com.pepo.notasapi.Auth.Entity;

import java.time.Instant;

import com.pepo.notasapi.Usuarios.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean revoked = false;

    public RefreshToken() {
        this.createdAt = Instant.now();
    }

    public RefreshToken(Usuario usuario, String token, Instant expiryDate) {
        this.usuario = usuario;
        this.token = token;
        this.expiryDate = expiryDate;
        this.createdAt = Instant.now();
        this.revoked = false;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}