package com.pepo.notasapi.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverride(name = "value", column = @Column(name = "email")) // Mapeia o campo 'value' para a coluna 'email' na tabela
public final class EmailVO {
    private final String value;

    public EmailVO() { this.value = ""; }
    
    public EmailVO(String value) { 
    	// Valida emails: deve ter formato "usuario@dominio.extensao" 
    	// com letras, números e alguns símbolos antes do @, 
    	// domínio com letras, números, pontos ou hífen, 
    	// e extensão com pelo menos 2 letras.
        if(!value.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) 
            throw new IllegalArgumentException("Email inválido");
        this.value = value;
    }
    @JsonValue
    public String getValue() { return value; }
}
