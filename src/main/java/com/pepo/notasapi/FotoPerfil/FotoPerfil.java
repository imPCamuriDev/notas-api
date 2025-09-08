package com.pepo.notasapi.FotoPerfil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pepo.notasapi.Usuarios.Usuario;

import jakarta.persistence.*;

@Entity
@Table(name = "foto_perfil")
public class FotoPerfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB") 
    private byte[] imagem;

    @Column(name = "tipo_arquivo", length = 50)
    private String tipoArquivo;
    
    public FotoPerfil() {
        super();
    }

    public FotoPerfil(Usuario usuario, byte[] imagem, String tipoArquivo) {
        this.usuario = usuario;
        this.imagem = imagem;
        this.tipoArquivo = tipoArquivo;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }


}