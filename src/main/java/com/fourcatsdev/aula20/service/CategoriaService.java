package com.fourcatsdev.aula20.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula20.modelo.Categoria;
import com.fourcatsdev.aula20.repository.CategoriaRepository;



@Service
public class CategoriaService {

	
	@Autowired //injeçao de dependencia
	private CategoriaRepository categoriaRepository;
	
	public Categoria gravar(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	public Categoria buscarCategoriaPorId(Long id) {
		Optional<Categoria> opt = categoriaRepository.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new IllegalArgumentException("Categoria com id : " + id + " não existe");
		}
	}
	
	
	public void apagarCategoriaPorId(Long id) {
		Categoria categoria = buscarCategoriaPorId(id);
		categoriaRepository.delete(categoria);		
	}
	
	public void alterarCategoria(Categoria categoria) {
		categoriaRepository.save(categoria);		
	}

	
}
