package com.fourcatsdev.aula20.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula20.exception.EquipamentoNotFoundException;
import com.fourcatsdev.aula20.modelo.Categoria;
import com.fourcatsdev.aula20.modelo.Equipamento;
import com.fourcatsdev.aula20.modelo.Estado;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.service.CategoriaService;
import com.fourcatsdev.aula20.service.EquipamentoService;
import com.fourcatsdev.aula20.service.EstadoService;
import com.fourcatsdev.aula20.service.PedidoService;
import com.fourcatsdev.aula20.service.UsuarioService;


@Controller
@RequestMapping("/equipamento")
public class EquipamentoController {

	@Autowired
	private EquipamentoService equipamentoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PedidoService pedidoService;
	
	//listar para o admin
	@GetMapping("/admin/listar")
    public String listarEquipamentosAdmin(Model model) {	
		List<Equipamento> equipamentos = equipamentoService.buscarTodosEquipamentos();
		model.addAttribute("equipamentos",equipamentos);
		return "/auth/admin/admin-listar-equipamento";
    }
	
	//listar para o user
	@GetMapping("/listar")
    public String listarEquipamentos(Model model) {	
		List<Equipamento> equipamentos = equipamentoService.buscarTodosEquipamentos();
		model.addAttribute("equipamentos",equipamentos);
		return "/auth/user/user-listar-equipamento";
    }
	
	//buscar para o user
	
	@PostMapping("/buscar")
    public String buscarEquipamentos(Model model, @Param("nome") String nome) {	
		if (nome == null) {
			return "redirect:/equipamento/listar";
		}
		List<Equipamento> equipamentos = equipamentoService.buscarTodosEquipamentosPorNome(nome);
		model.addAttribute("equipamentos",equipamentos);
		return "/auth/user/user-listar-equipamento";
    }
	
	
	//buscar para o admin
	@PostMapping("/admin/buscar")
    public String buscarEquipamentosAdmin(Model model, @Param("nome") String nome) {	
		if (nome  == null) {
			return "redirect:/equipamento/admin/listar";
		}
		List<Equipamento> equipamentos = equipamentoService.buscarTodosEquipamentosPorNome(nome);
		model.addAttribute("equipamentos",equipamentos);
		return "/auth/admin/admin-listar-equipamento";
    }

	
	@GetMapping("/admin/novo")
    public String novoEquipamento(Model model) {	
		Equipamento equipamento = new Equipamento();
		model.addAttribute("equipamento",equipamento);
		List<Categoria> categorias = categoriaService.listar();
		model.addAttribute("categorias",categorias);
		return "/auth/admin/admin-novo-equipamento";
    }
	
	@PostMapping("/admin/gravar")
	public String gravarEquipamento(@ModelAttribute("equipamento") @Valid Equipamento equipamento,
			BindingResult erros,
			RedirectAttributes attributes) {
		if(erros.hasErrors()) {
			return "/auth/admin/admin-novo-equipamento";
		}
		Estado estado = estadoService.buscarPorNome("Disponível");
		equipamento.setEstado(estado);
		equipamentoService.criarEquipamento(equipamento);
		attributes.addFlashAttribute("mensagem", "Equipamento salvo com sucesso!");
        return "redirect:/equipamento/admin/listar";
    }
	
	@GetMapping("/admin/apagar/{id}")
    public String apagarEquipamento(@PathVariable("id") long id, RedirectAttributes attributes) {	
		try {
			equipamentoService.apagarEquipamento(id);
		} catch (EquipamentoNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
		}
        return "redirect:/equipamento/admin/listar";
    }
	
	
	@GetMapping("/admin/editar/{id}")
    public String editarForm(@PathVariable("id") long id, RedirectAttributes attributes,
    		Model model) {	
		try {
			Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(id);
			model.addAttribute("equipamento", equipamento);
			List<Categoria> categorias = categoriaService.listar();
			model.addAttribute("categorias",categorias);
			List<Estado> estados = estadoService.listarEstados();
			model.addAttribute("estados", estados);
			return "/auth/admin/admin-alterar-equipamento";
		} catch (EquipamentoNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
		}
        return "redirect:/equipamento/admin/listar";
    }
	
	@PostMapping("/admin/editar/{id}")
	public String editarEquipamento(@PathVariable("id") long id, 
								@ModelAttribute("equipamento") @Valid Equipamento equipamento, 
								BindingResult erros) {
		if (erros.hasErrors()) {
			equipamento.setId(id);
	        return "/auth/admin/admin-alterar-equipamento";
	    }
		equipamentoService.alterarEquipamento(equipamento);
		return "redirect:/equipamento/admin/listar";
	}
	
	
	@PostMapping("/pedidos")
	public String postRequests(@CurrentSecurityContext(expression = "authentication.name") String login, 
			@RequestParam(value = "idsEquipamentos", required = false) int[] ids, @RequestParam(value = "dataEmprestimo") String dataEmprestimo, 
	@RequestParam(value = "dataDevolucao") String dataDevolucao, @RequestParam(value = "finalidade") String finalidade) throws ParseException, EquipamentoNotFoundException {
		
		for(int i = 0; i < ids.length; i++) {
			System.out.println(ids[i]);
		}
		
		Usuario usuario = usuarioService.buscarUsuarioPorLogin(login);
		pedidoService.gravar(usuario, dataEmprestimo, dataDevolucao, finalidade, ids);
		
		return "redirect:/usuario/index";
		
		//sql do banco 
		//select distinct usuario_id, data_pedido::date from pedido;
	}
	
		
}
