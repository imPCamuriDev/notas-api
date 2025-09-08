package com.pepo.notasapi.FotoPerfil.DTO;

public class FotoBase64DTO {
    private String base64;
    private String tipoArquivo;


    public FotoBase64DTO() {}

    public FotoBase64DTO(String base64, String tipoArquivo) {
        this.base64 = base64;
        this.tipoArquivo = tipoArquivo;
    }

    // Getters e Setters
    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
}