package com.fourcatsdev.aula20.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula20.modelo.Categoria;
import com.fourcatsdev.aula20.modelo.Equipamento;
import com.fourcatsdev.aula20.modelo.Papel;
import com.fourcatsdev.aula20.modelo.Pedido;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.service.CategoriaService;
import com.fourcatsdev.aula20.service.EquipamentoService;
import com.fourcatsdev.aula20.service.I18nService;
import com.fourcatsdev.aula20.service.PapelService;
import com.fourcatsdev.aula20.service.PedidoService;
import com.fourcatsdev.aula20.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PapelService papelService;

	@Autowired
	private EquipamentoService equipamentoService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private I18nService i18nService;

	/**
	 * Método que verifica qual papel o usuário tem na aplicação
	 */
	private boolean temAutorizacao(Usuario usuario, String papel) {
		for (Papel pp : usuario.getPapeis()) {
			if (pp.getPapel().equals(papel)) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/index")
	public String index(@CurrentSecurityContext(expression = "authentication.name") String login, Model model) {

		Usuario usuario = usuarioService.buscarUsuarioPorLogin(login);
		prepararDashboard(model, usuario);

		String redirectURL = "";
		if (temAutorizacao(usuario, "ADMIN")) {
			redirectURL = "/auth/admin/admin-index";
		} else if (temAutorizacao(usuario, "USER")) {
			redirectURL = "/auth/user/user-index";
		} else if (temAutorizacao(usuario, "BIBLIOTECARIO")) {
			redirectURL = "/auth/biblio/biblio-index";
		}

		return redirectURL;
	}

	private void prepararDashboard(Model model, Usuario usuarioLogado) {
		List<Usuario> usuarios = usuarioService.listarUsuario();
		List<Equipamento> equipamentos = equipamentoService.buscarTodosEquipamentos();
		List<Categoria> categorias = categoriaService.listar();
		List<Pedido> pedidos = pedidoService.listar();

		long usuariosAtivos = usuarios.stream().filter(Usuario::isAtivo).count();
		double valorPatrimonio = equipamentos.stream().mapToDouble(Equipamento::getValor).sum();
		long pedidosEmAnalise = pedidos.stream()
				.filter(pedido -> contemTexto(nomeEstadoPedido(pedido), "analise")
						|| contemTexto(nomeEstadoPedido(pedido), "análise"))
				.count();

		model.addAttribute("usuarioLogado", usuarioLogado);
		model.addAttribute("totalUsuarios", usuarios.size());
		model.addAttribute("usuariosAtivos", usuariosAtivos);
		model.addAttribute("totalEquipamentos", equipamentos.size());
		model.addAttribute("totalCategorias", categorias.size());
		model.addAttribute("totalPedidos", pedidos.size());
		model.addAttribute("pedidosEmAnalise", pedidosEmAnalise);
		model.addAttribute("valorPatrimonio", NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorPatrimonio));
		model.addAttribute("pedidosPorSituacao", montarGrafico(agruparPedidosPorSituacao(pedidos)));
		model.addAttribute("equipamentosPorCategoria", montarGrafico(agruparEquipamentosPorCategoria(equipamentos)));
		model.addAttribute("pedidosRecentes", pedidos.stream()
				.sorted(Comparator.comparing(Pedido::getDataPedido, Comparator.nullsLast(Comparator.reverseOrder())))
				.limit(5)
				.collect(Collectors.toList()));
	}

	private Map<String, Long> agruparPedidosPorSituacao(List<Pedido> pedidos) {
		return pedidos.stream().collect(Collectors.groupingBy(this::nomeEstadoPedido, LinkedHashMap::new, Collectors.counting()));
	}

	private Map<String, Long> agruparEquipamentosPorCategoria(List<Equipamento> equipamentos) {
		return equipamentos.stream().collect(Collectors.groupingBy(this::nomeCategoria, LinkedHashMap::new, Collectors.counting()));
	}

	private List<Map<String, Object>> montarGrafico(Map<String, Long> dados) {
		List<Map<String, Object>> grafico = new ArrayList<>();
		long maiorValor = dados.values().stream().mapToLong(Long::longValue).max().orElse(1L);

		for (Map.Entry<String, Long> item : dados.entrySet()) {
			Map<String, Object> linha = new LinkedHashMap<>();
			linha.put("nome", item.getKey());
			linha.put("total", item.getValue());
			linha.put("percentual", maiorValor == 0 ? 0 : Math.max(8, Math.round((item.getValue() * 100.0) / maiorValor)));
			grafico.add(linha);
		}

		return grafico;
	}

	private String nomeEstadoPedido(Pedido pedido) {
		if (pedido.getEstadoPedido() == null || pedido.getEstadoPedido().getNome() == null) {
			return "Sem situação";
		}
		return pedido.getEstadoPedido().getNome();
	}

	private String nomeCategoria(Equipamento equipamento) {
		if (equipamento.getCategoria() == null || equipamento.getCategoria().getNome() == null) {
			return "Sem categoria";
		}
		return equipamento.getCategoria().getNome();
	}

	private boolean contemTexto(String texto, String trecho) {
		return texto != null && texto.toLowerCase(Locale.ROOT).contains(trecho);
	}

	@GetMapping("/novo")
	public String adicionarUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "/publica-criar-usuario";
	}

	@PostMapping("/salvar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result, Model model,
			RedirectAttributes attributes, Locale locale) {
		if (result.hasErrors()) {
			return "/publica-criar-usuario";
		}
		Usuario usr = usuarioService.buscarUsuarioPorLogin(usuario.getLogin());
		
		String msn = null;
		if (usr != null) {
			msn = i18nService.buscarMensagem("user.controller.already", locale);
			model.addAttribute("loginExiste", msn);
			return "/publica-criar-usuario";
		}
		
		usuarioService.gravarUsuario(usuario);
		msn = i18nService.buscarMensagem("user.controller.saved", locale);
		
		attributes.addFlashAttribute("mensagem", msn);
		return "redirect:/usuario/novo";
	}

	@RequestMapping("/admin/listar")
	public String listarUsuario(Model model) {
		List<Usuario> usuarios = usuarioService.listarUsuario();
 		model.addAttribute("usuarios", usuarios);		
		return "/auth/admin/admin-listar-usuario";
	}


	
	@GetMapping("/admin/apagar/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		usuarioService.apagarUsuarioPorId(id);				
	    return "redirect:/usuario/admin/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id, Model model) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);
	    model.addAttribute("usuario", usuario);	    
	    return "/auth/user/user-alterar-usuario";
	}

	@PostMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id, @Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) {
	    	usuario.setId(id);
	        return "/auth/user/user-alterar-usuario";
	    }
		usuarioService.alterarUsuario(usuario);
	    return "redirect:/usuario/admin/listar";
	}

	@GetMapping("/editarPapel/{id}")
	public String selecionarPapel(@PathVariable("id") long id, Model model) {
		Usuario usuario = usuarioService.buscarUsuarioPorId(id);		
	    model.addAttribute("usuario", usuario);	    
	    List<Papel> papeis = papelService.listarPapel();	    
	    model.addAttribute("listaPapeis", papeis);
	    
	    return "/auth/admin/admin-editar-papel-usuario";
	}

	@PostMapping("/editarPapel/{id}")
	public String atribuirPapel(@PathVariable("id") long idUsuario,
			@RequestParam(value = "pps", required = false) int[] pps, 
			Usuario usuario, RedirectAttributes attributes, Locale locale) {
		
		if (pps == null) {
			usuario.setId(idUsuario);
			String msn = i18nService.buscarMensagem("user.controller.role", locale);
			attributes.addFlashAttribute("mensagem", msn);
			return "redirect:/usuario/editarPapel/"+idUsuario;
		} else {
			usuarioService.atribuirPapelParaUsuario(idUsuario, pps, usuario.isAtivo());		
		}		
	    return "redirect:/usuario/admin/listar";
	}
}
