package com.fourcatsdev.aula20.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.fourcatsdev.aula20.modelo.Estado;


public interface EstadoRepository extends JpaRepository<Estado, Long> {
	Estado findByNome(String nome);
	List<Estado> findByNomeContainingIgnoreCase(String nome);
}
