package com.fourcatsdev.aula20.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.fourcatsdev.aula20.modelo.Estado;
import com.fourcatsdev.aula20.modelo.EstadoPedido;
import com.fourcatsdev.aula20.modelo.Papel;
import com.fourcatsdev.aula20.modelo.Usuario;
import com.fourcatsdev.aula20.repository.PapelRepository;
import com.fourcatsdev.aula20.repository.UsuarioRepository;
import com.fourcatsdev.aula20.service.EstadoPedidoService;
import com.fourcatsdev.aula20.service.EstadoService;
import com.fourcatsdev.aula20.service.MenuPermissaoService;

@Component
public class CarregadoraDados implements CommandLineRunner {

	@Autowired
	private PapelRepository papelRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private EstadoPedidoService estadoPedidoService;

	@Autowired
	private MenuPermissaoService menuPermissaoService;
	
	@Override
	public void run(String... args) throws Exception {
		
		String[] papeis = {"ADMIN", "USER"};

		for (String papelString: papeis) {
			Papel papel = papelRepository.findByPapel(papelString);
			if (papel == null) {
				papel = new Papel(papelString);
				papelRepository.save(papel);
			}
		}

		menuPermissaoService.inicializarPermissoes();

		Usuario admin = usuarioRepository.findByLogin("admin");
		if (admin == null) {
			Papel papelAdmin = papelRepository.findByPapel("ADMIN");

			admin = new Usuario();
			admin.setNome("Administrador");
			admin.setLogin("admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			admin.setAtivo(true);
			List<Papel> papeisAdmin = new ArrayList<>();
			papeisAdmin.add(papelAdmin);
			admin.setPapeis(papeisAdmin);

			usuarioRepository.save(admin);
		}

		String[] estados = { "Disponível", "Manutenção", "Emprestado" };

		for (String estadoString : estados) {
			Estado estado = estadoService.buscarPorNome(estadoString);
			if (estado == null) {
				estado = new Estado(estadoString);
				estadoService.gravar(estado);
			}
		}
		
		// Array de estados do pedido
		String[] estadosPedido = { "Aprovado", "Negado", "Em análise" };

		// Iterando sobre os estados do pedido
		for (String estadoString : estadosPedido) {
		    // Verificando se o estado do pedido já existe no banco de dados
		    EstadoPedido estadoPedido = estadoPedidoService.buscarPorNome(estadoString);
		    
		    // Se o estado do pedido não existir, criá-lo e gravá-lo no banco de dados
		    if (estadoPedido == null) {
		        estadoPedido = new EstadoPedido(estadoString);
		        estadoPedidoService.gravar(estadoPedido);
		    }
		    
		   
		}


		
		/*
		
		String[] estadosPedido = { "Aprovado", "Negado", "Em análise" };

		for (String estadoString : estadosPedido) {
			Estado estado = estadoService.buscarPorNome(estadoString);
			if (estado == null) {
				estado = new Estado(estadoString);
				estadoService.gravar(estado);
			}
		}
		*/
		
		
	}

}
