package com.pepo.notasapi.Usuarios;

import com.pepo.notasapi.ValueObjects.Email;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    @Embedded
    private Email email;

    @Column(nullable = false)
    private String password;

    @Column(name = "email_verificado", columnDefinition = "boolean default false")
    private Boolean emailVerificado = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEmailVerificado() {
		return emailVerificado;
	}

	public void setEmailVerificado(Boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}
    
    
    

    // @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private FotoPerfilEntities fotoPerfil;

    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private List<ItemsEntities> items;
}