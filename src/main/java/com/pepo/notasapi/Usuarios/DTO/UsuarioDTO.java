package com.pepo.notasapi.Usuarios.DTO;

import com.pepo.notasapi.FotoPerfil.DTO.FotoPerfilDTO;
import com.pepo.notasapi.ValueObjects.EmailVO;

public class UsuarioDTO {
	private Long id;
	private String nome;
	private EmailVO email;
	private FotoPerfilDTO fotoPerfil;

	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String nome, EmailVO email,
			FotoPerfilDTO fotoPerfil) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.fotoPerfil = fotoPerfil;
	}

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

	public EmailVO getEmail() {
		return email;
	}

	public void setEmail(EmailVO email) {
		this.email = email;
	}

	// public List<ItemDTO> getItens() {
	// 	return itens;
	// }

	// public void setItens(List<ItemDTO> itens) {
	// 	this.itens = itens;
	// }

	public FotoPerfilDTO getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(FotoPerfilDTO fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
}