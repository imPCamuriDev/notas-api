package com.pepo.notasapi.Itens;

import com.pepo.notasapi.Usuarios.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataLimite;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

	public Item() {
		super();
	}

	public Item(Long id, String descricao, LocalDateTime dataCriacao, LocalDateTime dataLimite, Usuario usuario) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataCriacao = dataCriacao;
		this.dataLimite = dataLimite;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(LocalDateTime dataLimite) {
		this.dataLimite = dataLimite;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
    
    
    
    
}
