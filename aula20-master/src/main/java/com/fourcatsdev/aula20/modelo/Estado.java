package com.fourcatsdev.aula20.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Estado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome do estado deve ser informado!")
	@Size(min = 3, message = "O nome do estado deve ter ao menos 3 caracteres!")
	private String nome;
	
	@OneToMany(mappedBy = "estado")
	private List<Equipamento> equipamentos;
	
	
	public Estado(String nome) {
		super();
		this.nome = nome;
	}

		
	public Estado() {
		super();
		// TODO Auto-generated constructor stub
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

	public List<Equipamento> getEquipamentos() {
		return equipamentos;
	}

	public void setEquipamentos(List<Equipamento> equipamentos) {
		this.equipamentos = equipamentos;
	}



	
	
	
	
	
	
	
	
	
	
	
}
