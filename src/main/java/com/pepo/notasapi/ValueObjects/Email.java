package com.pepo.notasapi.ValueObjects;

public final class Email {
    private final String value;
    public Email(String value) { 
    	// Valida emails: deve ter formato "usuario@dominio.extensao" 
    	// com letras, números e alguns símbolos antes do @, 
    	// domínio com letras, números, pontos ou hífen, 
    	// e extensão com pelo menos 2 letras.
        if(!value.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) 
            throw new IllegalArgumentException("Email inválido");
        this.value = value;
    }
    public String getValue() { return value; }
}
