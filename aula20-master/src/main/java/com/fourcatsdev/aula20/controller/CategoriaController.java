package com.fourcatsdev.aula20.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula20.modelo.Categoria;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.service.CategoriaService;


@Controller
@RequestMapping("/auth/admin/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	@RequestMapping("/listar")
	public String listarCategoria(Model model) {
		List<Categoria> categorias = categoriaService.listar();
 		model.addAttribute("categorias", categorias);		
		return "/auth/admin/admin-listar-categoria";
	}
	
	@GetMapping("/apagar/{id}")
	public String deleteCategoria(@PathVariable("id") long id, Model model) {
		categoriaService.apagarCategoriaPorId(id);				
	    return "redirect:/auth/admin/categoria/listar";
	}
	
	@GetMapping("/novo")
    public String novoCategoria(Model model) {	
		Categoria categoria = new Categoria();
		model.addAttribute("categoria",categoria);
        return "/auth/admin/admin-novo-categoria";
    }
	
	@PostMapping("/gravar")
	public String gravarCategoria(@ModelAttribute("categoria") @Valid Categoria categoria,
			BindingResult erros,
			RedirectAttributes attributes) {
		if(erros.hasErrors()) {
			return "/auth/admin/admin-novo-categoria";
		}
		categoriaService.gravar(categoria);
		attributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
        return "redirect:/auth/admin/categoria/listar";
    }
	
	@GetMapping("/editar/{id}")
	public String editarCategoria(@PathVariable("id") long id, Model model) {
		Categoria categoria = categoriaService.buscarCategoriaPorId(id);
	    model.addAttribute("categoria", categoria);	    
	    return "/auth/admin/admin-alterar-categoria";
	}

	@PostMapping("/editar/{id}")
	public String editarCategoria(@PathVariable("id") long id, @Valid Categoria categoria, BindingResult result) {
		if (result.hasErrors()) {
	    	categoria.setId(id);
	        return "/auth/admin/admin-alterar-categoria";
	    }
		categoriaService.alterarCategoria(categoria);
	    return "redirect:/auth/admin/categoria/listar";
	}
}
