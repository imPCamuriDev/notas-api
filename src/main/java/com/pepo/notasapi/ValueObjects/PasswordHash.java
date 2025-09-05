package com.pepo.notasapi.ValueObjects;

public final class PasswordHash {
    private final String hash;
    public PasswordHash(String rawPassword) {
        if(rawPassword.length() < 6) throw new IllegalArgumentException("Senha muito curta");
        this.hash = Integer.toHexString(rawPassword.hashCode());
    }
    public String getHash() { return hash; }
}