package com.pepo.notasapi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "email_verificado", columnDefinition = "boolean default false")
    private Boolean emailVerificado = false;

    // @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private FotoPerfilEntities fotoPerfil;

    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private List<ItemsEntities> items;
}