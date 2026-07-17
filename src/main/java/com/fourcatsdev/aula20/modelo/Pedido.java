package com.fourcatsdev.aula20.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Pedido {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date dataEmprestimo;
	
	private Date dataDevolucao;
	
	private Date dataPedido;
	
	private String finalidade;
	
	
	@ManyToOne //relacionamento de n pedidos para um usuario
	private Equipamento equipamentoPedido;
	
	
	@ManyToOne //relacionamento de n pedidos para um usuario
	private Usuario usuario;

	@ManyToOne //relacionamento de n pedidos para um estado
	private EstadoPedido estadoPedido;
	
	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	//getters e setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	
	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

	public Equipamento getEquipamentoPedido() {
		return equipamentoPedido;
	}

	public void setEquipamentoPedido(Equipamento equipamentoPedido) {
		this.equipamentoPedido = equipamentoPedido;
	}

		
	
	
}
