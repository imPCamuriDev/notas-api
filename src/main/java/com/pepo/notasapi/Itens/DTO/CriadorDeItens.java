package com.pepo.notasapi.Itens.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CriadorDeItens {
    
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;
    
    private LocalDateTime dataLimite;
    
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    // Construtores
    public CriadorDeItens() {
    }

    public CriadorDeItens(String descricao, LocalDateTime dataLimite, Long usuarioId) {
        this.descricao = descricao;
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}