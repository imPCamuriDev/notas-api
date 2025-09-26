package com.pepo.notasapi.FotoPerfil.DTO;

public class FotoBinarioDTO {
    private byte[] dadosBinarios;
    private String tipoArquivo;

    public FotoBinarioDTO() {}

    public FotoBinarioDTO(byte[] dadosBinarios, String tipoArquivo) {
        this.dadosBinarios = dadosBinarios;
        this.tipoArquivo = tipoArquivo;
    }

    public byte[] getDadosBinarios() {
        return dadosBinarios;
    }

    public void setDadosBinarios(byte[] dadosBinarios) {
        this.dadosBinarios = dadosBinarios;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
}