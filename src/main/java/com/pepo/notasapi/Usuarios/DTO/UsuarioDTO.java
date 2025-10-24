package com.pepo.notasapi.Usuarios.DTO;

import com.pepo.notasapi.FotoPerfil.DTO.FotoPerfilDTO;
import com.pepo.notasapi.ValueObjects.EmailVO;

import lombok.Data;

@Data
public class UsuarioDTO {
	private Long id;
	private String nome;
	private EmailVO email;
	private FotoPerfilDTO fotoPerfil;


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