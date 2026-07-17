package com.fourcatsdev.aula20.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourcatsdev.aula20.modelo.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
