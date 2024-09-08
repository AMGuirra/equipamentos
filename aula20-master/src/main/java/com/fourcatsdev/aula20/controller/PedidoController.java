package com.fourcatsdev.aula20.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fourcatsdev.aula20.modelo.EstadoPedido;
import com.fourcatsdev.aula20.modelo.Pedido;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.repository.PedidoResponseData;
import com.fourcatsdev.aula20.repository.PedidoResponseEquipamento;
import com.fourcatsdev.aula20.service.EstadoPedidoService;
import com.fourcatsdev.aula20.service.PedidoService;
import com.fourcatsdev.aula20.service.UsuarioService;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private EstadoPedidoService estadoPedidoService;
	
	/*
	 * 
	 * método alterarEstado recebe o id do pedido e o id
	 * pedidoBuscado =  vai no banco busca o pedido por id
	 * se for aprovado 
	 * estadoPedidoBuscado = vai no banco busca EstadoPedido pelo id de aprovado
	 *   pedidoBuscado.setEstadoPedido(estadoPedidoBuscado )
	 *   salvar(pedidoBuscado)
	 * 
	 * */
	
	//listar para o admin
	@RequestMapping("/admin/listar")
	public String listarPedidoAdmin(Model model) {
		List<PedidoResponseData> pedidos = 	pedidoService.buscarPedido();
		model.addAttribute("pedidos", pedidos);
		for(PedidoResponseData p:pedidos) {
			System.out.println(p.getId());
			System.out.println(p.getData());
		}
		return "/auth/admin/admin-listar-pedido";
	}
	
	//listar para o user(listar pedidos apenas do usuário)
	 
	@RequestMapping("/listar")
	public String listarPedido(Model model, @CurrentSecurityContext(expression = "authentication.name") String login){
	   
		Usuario usuario = usuarioService.buscarUsuarioPorLogin(login);
		// Buscando o pedido pelo ID
		List<PedidoResponseData> pedidos = pedidoService.buscarPedidoDeUsuario(usuario.getId());
		
	    // Adicionando o pedido ao modelo
	    model.addAttribute("pedido", pedidos);
	    for(PedidoResponseData p:pedidos) {
			System.out.println(p.getId());
			System.out.println(p.getData());
		}
	    return "/auth/user/user-listar-pedido";
	}
	
	
	@GetMapping("/apagar/{id}")
	public String deletePedido(@PathVariable("id") long id, Model model) {
		pedidoService.apagarPedidoPorId(id);				
	    return "redirect:/auth/admin/pedido/listar";
	}
	
	
	
	
	@RequestMapping("/ver/{id}/{data}")
	public String VerPedido(@PathVariable("id") Long id, @PathVariable("data") String data, Model model) {
		System.out.println("------------------> "+ data);
		List<PedidoResponseEquipamento> equipamentos = pedidoService.buscarPedidoEquipamento(id, data);
		for(PedidoResponseEquipamento e:equipamentos) {
			System.out.println(e.getId());
			System.out.println(e.getFabricante());
		}
 		model.addAttribute("equipamentos", equipamentos);		
		return "/auth/admin/admin-ver-pedido";
	}

	 @RequestMapping("/admin/aprovar/{estado}/{idPedido}")
	    public String aprovar(@PathVariable("estado") int estado, @PathVariable("idPedido") Long idPedido, RedirectAttributes attributes) {
	        // Imprime no console o estado e o ID do pedido recebidos como parâmetros
	        System.out.println("Estado recebido: " + estado);
	        System.out.println("ID do Pedido recebido: " + idPedido);

	        // Busca o pedido pelo ID fornecido
	        Pedido pedidoBuscado = pedidoService.buscarPedidoPorId(idPedido);
	        if (pedidoBuscado != null) {  // Verifica se o pedido foi encontrado
	            EstadoPedido estadoPedidoBuscado = null;

	            // Define o estado do pedido com base no valor do parâmetro "estado"
	            switch (estado) {
	                case 1:
	                    estadoPedidoBuscado = estadoPedidoService.buscarPorNome("Aprovado");  // Busca o estado "Aprovado"
	                    break;
	                case 2:
	                    estadoPedidoBuscado = estadoPedidoService.buscarPorNome("Negado");  // Busca o estado "Negado"
	                    break;
	                case 3:
	                    estadoPedidoBuscado = estadoPedidoService.buscarPorNome("Em Análise");  // Busca o estado "Em Análise"
	                    break;
	                default:
	                    // Se o estado não for 1, 2 ou 3, adiciona uma mensagem de erro e redireciona para a lista de pedidos
	                    attributes.addFlashAttribute("erro", "Ação inválida!");
	                    return "redirect:/pedido/admin/listar";
	            }

	            // Verifica se o estado do pedido foi encontrado
	            if (estadoPedidoBuscado != null) {
	                // Atualiza o estado do pedido e salva as alterações
	                pedidoBuscado.setEstadoPedido(estadoPedidoBuscado);
	                pedidoService.salvar(pedidoBuscado);
	                // Adiciona uma mensagem de sucesso
	                attributes.addFlashAttribute("mensagem", "Estado do pedido atualizado com sucesso!");
	            } else {
	                // Se o estado do pedido não foi encontrado, adiciona uma mensagem de erro
	                attributes.addFlashAttribute("erro", "Estado do pedido não encontrado!");
	            }
	        } else {
	            // Se o pedido não foi encontrado, adiciona uma mensagem de erro
	            attributes.addFlashAttribute("erro", "Pedido não encontrado!");
	        }

	        // Redireciona para a lista de pedidos
	        return "redirect:/pedido/admin/listar";
	    }
	


	

	//implementar um ver pedido para o usuário também
	
	//alterar para o pedido ser feito com as informações preenchidas pelo usuario na user-listar-equipamento 
	/*@GetMapping("/novo")
    public String novoPedido(Model model) {	
		Pedido pedido = new Pedido();
		model.addAttribute("pedido",pedido);
        return "/auth/admin/admin-novo-pedido";
    }
	*/
	/*
	@PostMapping("/gravar")
	public String gravarPedido(@ModelAttribute("pedido") @Valid Pedido pedido,
			BindingResult erros,
			RedirectAttributes attributes) {
		if(erros.hasErrors()) {
			return "/auth/admin/admin-novo-pedido";
		}
		pedidoService.gravar(pedido);
		attributes.addFlashAttribute("mensagem", "Pedido salva com sucesso!");
        return "redirect:/auth/admin/pedido/listar";
    }
	*/
	//verificar se editar será usado
	/*
	@GetMapping("/editar/{id}")
	public String editarPedido(@PathVariable("id") long id, Model model) {
		Pedido pedido = pedidoService.buscarPedidoPorId(id);
	    model.addAttribute("pedido", pedido);	    
	    return "/auth/admin/admin-alterar-pedido";
	}

	@PostMapping("/editar/{id}")
	public String editarPedido(@PathVariable("id") long id, @Valid Pedido pedido, BindingResult result) {
		if (result.hasErrors()) {
	    	pedido.setId(id);
	        return "/auth/admin/admin-alterar-pedido";
	    }
		pedidoService.alterarPedido(pedido);
	    return "redirect:/auth/admin/pedido/listar";
	}
	*/
}
