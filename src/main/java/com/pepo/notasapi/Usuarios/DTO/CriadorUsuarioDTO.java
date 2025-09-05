package com.pepo.notasapi.Usuarios.DTO;

import com.pepo.notasapi.ValueObjects.Email;

public class CriadorUsuarioDTO {
    private String nome;
    private Email email;
    private String password;

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
    
    
    
}
