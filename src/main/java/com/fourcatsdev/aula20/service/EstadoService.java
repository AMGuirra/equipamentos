package com.fourcatsdev.aula20.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula20.modelo.Estado;
import com.fourcatsdev.aula20.repository.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository estadoRepository;
	
	public Estado gravar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public Estado buscarPorNome(String nome) {
		return estadoRepository.findByNome(nome);
	}
	
	public List<Estado> listarEstados(){
	    return estadoRepository.findAll();
	}

	
}
