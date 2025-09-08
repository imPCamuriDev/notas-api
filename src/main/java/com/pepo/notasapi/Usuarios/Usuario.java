package com.pepo.notasapi.Usuarios;

import java.util.List;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

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
    private EmailVO email;

    @Column(nullable = false)
	@Embedded
    private PasswordHashVO password;

    @Column(name = "email_verificado", columnDefinition = "boolean default false", nullable = false)
    private Boolean emailVerificado = false;

	@Column
	@JoinColumn(name = "item_id")
	@OneToMany
	private List<Item> items;

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

	public PasswordHashVO getPassword() {
		return password;
	}
	
	public void setPassword(PasswordHashVO password) {
		this.password = password;
	}

	public Boolean getEmailVerificado() {
		return emailVerificado;
	}

	public void setEmailVerificado(Boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	
    
    
    

    // @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private FotoPerfilEntities fotoPerfil;

    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private List<ItemsEntities> items;
}