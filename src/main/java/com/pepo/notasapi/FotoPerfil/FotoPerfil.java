package com.pepo.notasapi.FotoPerfil;

import com.pepo.notasapi.Usuarios.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "foto_perfil")
public class FotoPerfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Lob
    @Column(name = "dados_binarios", columnDefinition = "LONGBLOB")
    private byte[] dadosBinarios;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    // Construtores
    public FotoPerfil() {}

    public FotoPerfil(Usuario usuario, byte[] dadosBinarios, String tipoArquivo) {
        this.usuario = usuario;
        this.dadosBinarios = dadosBinarios;
        this.tipoArquivo = tipoArquivo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public byte[] getDadosBinarios() { return dadosBinarios; }
    public void setDadosBinarios(byte[] dadosBinarios) { this.dadosBinarios = dadosBinarios; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }
}