package com.pepo.notasapi.Itens.DTO;

import java.time.LocalDateTime;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Usuarios.Usuario;

public class ItemDTO {
    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataLimite;
    private Usuario usuario_id;
 
    
    public ItemDTO(Long id, String descricao, LocalDateTime dataCriacao, LocalDateTime dataLimite, Usuario usuario_id) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.usuario_id = usuario_id;
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

    public Usuario getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Usuario usuario_id) {
        this.usuario_id = usuario_id;
    }


    

}
