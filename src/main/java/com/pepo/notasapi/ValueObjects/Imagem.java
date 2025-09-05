package com.pepo.notasapi.ValueObjects;

public final class Imagem {
    private final byte[] dados;
    public Imagem(byte[] dados) { this.dados = dados; }
    public byte[] getDados() { return dados; }
}