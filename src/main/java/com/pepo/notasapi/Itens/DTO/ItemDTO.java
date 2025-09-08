package com.pepo.notasapi.Itens.DTO;

import java.time.LocalDateTime;


public class ItemDTO {
    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataLimite;
    private Long usuarioId;
 
    
    public ItemDTO(Long id, String descricao, LocalDateTime dataCriacao, LocalDateTime dataLimite, Long usuarioId) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }
    
    public ItemDTO() {
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
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
