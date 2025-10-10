package com.pepo.notasapi.Auth.DTO;

public class AuthResponse {

    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long userId;
    private String nome;
    private String email;

    public AuthResponse() {}

    // Construtor completo com refreshToken
    public AuthResponse(String token, String refreshToken, Long userId, String nome, String email) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.nome = nome;
        this.email = email;
    }

    // Construtor sem refreshToken (compatibilidade)
    public AuthResponse(String token, Long userId, String nome, String email) {
        this.token = token;
        this.userId = userId;
        this.nome = nome;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}