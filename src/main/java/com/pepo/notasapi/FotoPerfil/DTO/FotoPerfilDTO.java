package com.pepo.notasapi.FotoPerfil.DTO;

public class FotoPerfilDTO {
    private Long id;
    private String tipoArquivo;
    
    public FotoPerfilDTO() {}
    
    public FotoPerfilDTO(Long id, String tipoArquivo) {
        this.id = id;
        this.tipoArquivo = tipoArquivo;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }
}