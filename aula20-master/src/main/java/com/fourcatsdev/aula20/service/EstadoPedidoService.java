package com.fourcatsdev.aula20.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.fourcatsdev.aula20.modelo.EstadoPedido;
import com.fourcatsdev.aula20.repository.EstadoPedidoRepository;


@Service
public class EstadoPedidoService {

	
	@Autowired
	private EstadoPedidoRepository estadopedidoRepository;
	
	public EstadoPedido gravar(EstadoPedido estadopedido) {
		return estadopedidoRepository.save(estadopedido);
	}
	
	public EstadoPedido buscarPorNome(String nome) {
		return estadopedidoRepository.findByNome(nome);
	}
	
	public List<EstadoPedido> listarEstadosPedidos(){
	    return estadopedidoRepository.findAll();
	}
}
