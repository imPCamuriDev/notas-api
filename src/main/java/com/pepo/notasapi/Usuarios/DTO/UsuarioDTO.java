package com.pepo.notasapi.Usuarios.DTO;

import com.pepo.notasapi.ValueObjects.EmailVO;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private EmailVO email;
    private Boolean emailVerificado;
    
	public UsuarioDTO() {
		super();
	}
	
	public UsuarioDTO(Long id, String nome, EmailVO email, Boolean emailVerificado) {
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
	public EmailVO getEmail() {
		return email;
	}
	public void setEmail(EmailVO email) {
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