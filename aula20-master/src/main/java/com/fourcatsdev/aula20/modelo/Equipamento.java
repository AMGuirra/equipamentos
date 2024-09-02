package com.fourcatsdev.aula20.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity //anotação de entidade
public class Equipamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome do equipamento deve ser informado!")
	@Size(min = 3, message = "O nome do equipamento deve ter ao menos 3 caracteres!")
	private String nome;
	
	
	@NotBlank(message = "O número de série do equipamento deve ser informado!")//pensar na obrigatoriedade de todo equipamento ter numero de serie.
	@Size(min = 3, message = "O número de série deve ter ao menos 3 caracteres!")
	private String numeroDeSerie;
	
	@NotBlank(message = "O nome do fabricante deve ser informado!")
	@Size(min = 3, message = "O nome do fabricante deve ter ao menos 3 caracteres!")
	private String fabricante;
	
	@Min(value = 1, message = "O valor unitário do produto deve ser maior que 1 real!")
	private double valor;
	
	@NotBlank(message = "A descrição do equipamento deve ser informada! Ex: utilidade do equipamento.")
	private String descricao;
	
	@ManyToOne //relacionamento de muitos eqp para uma categoria
	private Categoria categoria;
	
	@ManyToOne //relacionamento de muitos eqp para um estado
	private Estado estado;

	@OneToMany(mappedBy = "equipamento") //um equipamento para n emprestimos
	private List<Emprestimo> emprestimos;
	
	
	@OneToMany(mappedBy = "equipamentoPedido")
	private List<Pedido> pedidos;
	

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(List<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
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

	public String getNumeroDeSerie() {
		return numeroDeSerie;
	}

	public void setNumeroDeSerie(String numeroDeSerie) {
		this.numeroDeSerie = numeroDeSerie;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	
}

