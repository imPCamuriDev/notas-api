package com.pepo.notasapi.ValueObjects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverride(name = "password", column = @Column(name = "password"))
public final class PasswordHashVO {
    private final String password;

    public PasswordHashVO() {
        this.password = "";
    }

    public PasswordHashVO(PasswordHashVO passwordHash) {
        this.password = passwordHash.getHash();
    }

    public PasswordHashVO(String rawPassword) {
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