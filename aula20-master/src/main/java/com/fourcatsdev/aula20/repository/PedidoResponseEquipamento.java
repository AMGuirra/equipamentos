package com.fourcatsdev.aula20.repository;

import java.util.Date;

public interface PedidoResponseEquipamento {
	public Long getId();
	public Date getDevolucao();
	public Date getEmprestimo();
	public String getNome();
	public String getNumero();
	public String getFabricante();
	public Long getEstado();
	
}
