package com.pepo.notasapi.Usuarios.DTO;

import com.pepo.notasapi.ValueObjects.Email;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private Email email;
    private Boolean emailVerificado;
    
	public UsuarioDTO() {
		super();
	}
	public UsuarioDTO(Long id, String nome, Email email, Boolean emailVerificado) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.emailVerificado = emailVerificado;
	}
	
	//	ID
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	//	Nome
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	//	Email
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	
	//	Email Verificado
	public Boolean getEmailVerificado() {
		return emailVerificado;
	}
	public void setEmailVerificado(Boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}
    
    
    
}