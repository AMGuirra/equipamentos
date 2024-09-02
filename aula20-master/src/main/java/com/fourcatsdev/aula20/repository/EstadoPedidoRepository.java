package com.fourcatsdev.aula20.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.fourcatsdev.aula20.modelo.EstadoPedido;

public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long> {
	EstadoPedido findByNome(String nome);
	List<EstadoPedido> findByNomeContainingIgnoreCase(String nome);

}
