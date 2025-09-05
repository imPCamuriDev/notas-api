package com.pepo.notasapi.ValueObjects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverride(name = "password", column = @Column(name = "password"))
public final class PasswordHash {
    private final String password;

    public PasswordHash() {
        this.password = "";
    }

    public PasswordHash(PasswordHash passwordHash) {
        this.password = passwordHash.getHash();
    }

    public PasswordHash(String rawPassword) {
        if (rawPassword.length() < 6) {
            throw new IllegalArgumentException("Senha muito curta");
        }

        else {
            this.password = Integer.toHexString(rawPassword.hashCode());
        }
    }

    public String getHash() {
        return password;
    }
}