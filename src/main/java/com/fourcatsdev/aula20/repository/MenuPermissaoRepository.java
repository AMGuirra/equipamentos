package com.fourcatsdev.aula20.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fourcatsdev.aula20.modelo.MenuPermissao;
import com.fourcatsdev.aula20.modelo.Papel;

public interface MenuPermissaoRepository extends JpaRepository<MenuPermissao, Long> {
	MenuPermissao findByPapelAndMenu(Papel papel, String menu);
	List<MenuPermissao> findByPapel(Papel papel);
}
