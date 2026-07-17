package com.fourcatsdev.aula20.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula20.service.MenuPermissaoService;
import com.fourcatsdev.aula20.service.PapelService;

@Controller
@RequestMapping("/admin/menus")
public class MenuPermissaoController {

	@Autowired
	private MenuPermissaoService menuPermissaoService;

	@Autowired
	private PapelService papelService;

	@GetMapping
	public String listar(Model model) {
		menuPermissaoService.inicializarPermissoes();
		model.addAttribute("papeis", papelService.listarPapel());
		model.addAttribute("menus", menuPermissaoService.listarMenus());
		return "/auth/admin/admin-gerenciar-menus";
	}

	@PostMapping
	public String salvar(@RequestParam(value = "permitidos", required = false) List<String> permitidos,
			RedirectAttributes attributes) {
		menuPermissaoService.salvarPermissoes(permitidos);
		attributes.addFlashAttribute("mensagem", "Permissões de menu salvas com sucesso!");
		return "redirect:/admin/menus";
	}
}
