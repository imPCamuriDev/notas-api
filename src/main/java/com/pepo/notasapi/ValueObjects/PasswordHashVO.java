package com.pepo.notasapi.ValueObjects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@AttributeOverride(name = "password", column = @Column(name = "password"))
public final class PasswordHashVO {
    private final String password;
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public PasswordHashVO() {
        this.password = "";
    }

    public PasswordHashVO(PasswordHashVO passwordHash) {
        this.password = passwordHash.getHash();
    }

    // Construtor que recebe senha já hashada (para quando carregar do banco)
    public static PasswordHashVO fromHash(String hashedPassword) {
        PasswordHashVO vo = new PasswordHashVO();
        java.lang.reflect.Field field;
        try {
            field = PasswordHashVO.class.getDeclaredField("password");
            field.setAccessible(true);
            field.set(vo, hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar PasswordHashVO", e);
        }
        return vo;
    }

    // Construtor que recebe senha raw e faz o hash
    public PasswordHashVO(String rawPassword) {
        List<String> erros = validarSenha(rawPassword);
        
        if (!erros.isEmpty()) {
            throw new IllegalArgumentException("Senha inválida. Requisitos não atendidos: " + String.join(", ", erros));
        }

        this.password = encoder.encode(rawPassword);
    }

    private List<String> validarSenha(String senha) {
        List<String> erros = new ArrayList<>();
        
        if (senha == null || senha.length() < 8) {
            erros.add("mínimo de 8 caracteres");
        }
        
        if (senha != null) {
            if (!senha.matches(".*\\d.*")) {
                erros.add("pelo menos um número");
            }
            
            if (!senha.matches(".*[A-Z].*")) {
                erros.add("pelo menos uma letra maiúscula");
            }
            
            if (!senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                erros.add("pelo menos um caractere especial");
            }
        }
        
        return erros;
    }

    public String getHash() {
        return password;
    }
    
    public boolean matches(String rawPassword) {
        return encoder.matches(rawPassword, this.password);
    }
}