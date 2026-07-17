package com.fourcatsdev.aula20.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.fourcatsdev.aula20.modelo.MenuPermissao;
import com.fourcatsdev.aula20.modelo.Papel;
import com.fourcatsdev.aula20.repository.MenuPermissaoRepository;
import com.fourcatsdev.aula20.repository.PapelRepository;

@Service
public class MenuPermissaoService {

	public static class MenuOpcao {
		private String chave;
		private String nome;

		public MenuOpcao(String chave, String nome) {
			this.chave = chave;
			this.nome = nome;
		}

		public String getChave() {
			return chave;
		}

		public String getNome() {
			return nome;
		}
	}

	@Autowired
	private MenuPermissaoRepository menuPermissaoRepository;

	@Autowired
	private PapelRepository papelRepository;

	public List<MenuOpcao> listarMenus() {
		List<MenuOpcao> menus = new ArrayList<>();
		menus.add(new MenuOpcao("INICIAL", "Inicial"));
		menus.add(new MenuOpcao("USUARIOS", "Usuários"));
		menus.add(new MenuOpcao("CATEGORIAS", "Categorias"));
		menus.add(new MenuOpcao("EQUIPAMENTOS_ADMIN", "Equipamentos (admin)"));
		menus.add(new MenuOpcao("PEDIDOS_ADMIN", "Pedidos de equipamentos (admin)"));
		menus.add(new MenuOpcao("EQUIPAMENTOS_USER", "Equipamentos (usuário)"));
		menus.add(new MenuOpcao("PEDIDOS_USER", "Pedidos de equipamentos (usuário)"));
		return menus;
	}

	public void inicializarPermissoes() {
		List<Papel> papeis = papelRepository.findAll();
		for (Papel papel : papeis) {
			for (MenuOpcao menu : listarMenus()) {
				MenuPermissao permissao = menuPermissaoRepository.findByPapelAndMenu(papel, menu.getChave());
				if (permissao == null) {
					permissao = new MenuPermissao(papel, menu.getChave(), permissaoPadrao(papel.getPapel(), menu.getChave()));
					menuPermissaoRepository.save(permissao);
				}
			}
		}
	}

	public boolean temPermissao(String papel, String menu) {
		Papel papelEncontrado = papelRepository.findByPapel(papel);
		if (papelEncontrado == null) {
			return false;
		}
		MenuPermissao permissao = menuPermissaoRepository.findByPapelAndMenu(papelEncontrado, menu);
		if (permissao == null) {
			return permissaoPadrao(papel, menu);
		}
		return permissao.isPermitido();
	}

	public boolean podeExibir(Authentication authentication, String menu) {
		return podeAcessar(authentication, menu);
	}

	public boolean podeAcessar(Authentication authentication, String menu) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			if ("ADMIN".equals(authority.getAuthority()) && "GERENCIAR_MENUS".equals(menu)) {
				return true;
			}
			if (temPermissao(authority.getAuthority(), menu)) {
				return true;
			}
		}
		return false;
	}

	public void salvarPermissoes(List<String> permitidos) {
		List<Papel> papeis = papelRepository.findAll();
		for (Papel papel : papeis) {
			for (MenuOpcao menu : listarMenus()) {
				MenuPermissao permissao = menuPermissaoRepository.findByPapelAndMenu(papel, menu.getChave());
				if (permissao == null) {
					permissao = new MenuPermissao(papel, menu.getChave(), false);
				}
				permissao.setPermitido(permitidos != null && permitidos.contains(chaveFormulario(papel.getPapel(), menu.getChave())));
				menuPermissaoRepository.save(permissao);
			}
		}
	}

	public String chaveFormulario(String papel, String menu) {
		return papel + "|" + menu;
	}

	private boolean permissaoPadrao(String papel, String menu) {
		if ("ADMIN".equals(papel)) {
			return menu.equals("INICIAL")
					|| menu.equals("USUARIOS")
					|| menu.equals("CATEGORIAS")
					|| menu.equals("EQUIPAMENTOS_ADMIN")
					|| menu.equals("PEDIDOS_ADMIN");
		}
		if ("USER".equals(papel)) {
			return menu.equals("INICIAL")
					|| menu.equals("EQUIPAMENTOS_USER")
					|| menu.equals("PEDIDOS_USER");
		}
		return false;
	}
}
