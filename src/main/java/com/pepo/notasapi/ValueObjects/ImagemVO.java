package com.pepo.notasapi.ValueObjects;

public final class ImagemVO {
    private final byte[] dados;
    public ImagemVO(byte[] dados) { this.dados = dados; }
    public byte[] getDados() { return dados; }
}