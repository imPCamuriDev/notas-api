package com.pepo.notasapi.Usuarios;

import java.util.List;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    @Embedded
    private EmailVO email;

    @Column(nullable = false)
	@Embedded
    private PasswordHashVO password;

    @Column(name = "email_verificado", columnDefinition = "boolean default false", nullable = false)
    private Boolean emailVerificado = false;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Item> items;
    
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FotoPerfil fotoPerfil;

    
}