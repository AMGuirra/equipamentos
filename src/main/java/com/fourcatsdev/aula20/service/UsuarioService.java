package com.fourcatsdev.aula20.service;

import java.util.List;

import com.fourcatsdev.aula20.modelo.Usuario;

public interface UsuarioService {

	public void apagarUsuarioPorId(Long id);
	public Usuario buscarUsuarioPorId(Long id);
	public Usuario buscarUsuarioPorLogin(String login);
	public Usuario gravarUsuario(Usuario usuario);
	public void alterarUsuario(Usuario usuario);
	public void alterarDadosUsuario(Long id, Usuario dados);
	public void alterarSenhaUsuario(Long id, String senha);
	public List<Usuario> listarUsuario();
	public void atribuirPapelParaUsuario(long idUsuario, int[] idsPapeis, boolean isAtivo);
}
