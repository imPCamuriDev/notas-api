package com.pepo.notasapi.Usuarios.DTO;

import java.util.List;

import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.ValueObjects.EmailVO;

public class UsuarioDTO {

	private Long id;
	private String nome;
	private EmailVO email;
	private Boolean emailVerificado;
	private List<ItemDTO> itens;

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(Long id, String nome, EmailVO email, Boolean emailVerificado, List<ItemDTO> itens) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.emailVerificado = emailVerificado;
		this.itens = itens;
	}

	// ID
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Nome
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	// Email
	public EmailVO getEmail() {
		return email;
	}

	public void setEmail(EmailVO email) {
		this.email = email;
	}

	// Email Verificado
	public Boolean getEmailVerificado() {
		return emailVerificado;
	}

	public void setEmailVerificado(Boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}

	public List<ItemDTO> getItens() {
		return itens;
	}

	public void setItens(List<ItemDTO> itens) {
		this.itens = itens;
	}

	

}