package com.fourcatsdev.aula20.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fourcatsdev.aula20.repository.UsuarioRepository;



@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	private LoginSucesso loginSucesso;
	
	@Bean
	public BCryptPasswordEncoder gerarCriptografia() {
		BCryptPasswordEncoder criptografia = new BCryptPasswordEncoder();
		return criptografia;
	}
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		DetalheUsuarioServico detalheDoUsuario = new DetalheUsuarioServico(usuarioRepository);
		return detalheDoUsuario;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/admin/menus/**").hasAnyAuthority("ADMIN")
		.antMatchers("/usuario/index").authenticated()
		.antMatchers("/auth/user/*").hasAnyAuthority("USER","ADMIN","BIBLIOTECARIO")
		.antMatchers("/auth/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/auth/biblio/*").hasAnyAuthority("BIBLIOTECARIO")
		.antMatchers("/usuario/admin/**", "/usuario/editarPapel/**").access("@menuPermissaoService.podeAcessar(authentication, 'USUARIOS')")
		.antMatchers("/auth/admin/categoria/**").access("@menuPermissaoService.podeAcessar(authentication, 'CATEGORIAS')")
		.antMatchers("/equipamento/admin/**").access("@menuPermissaoService.podeAcessar(authentication, 'EQUIPAMENTOS_ADMIN')")
		.antMatchers("/pedido/admin/**", "/pedido/ver/**", "/pedido/apagar/**").access("@menuPermissaoService.podeAcessar(authentication, 'PEDIDOS_ADMIN')")
		.antMatchers("/equipamento/listar", "/equipamento/buscar", "/equipamento/pedidos").access("@menuPermissaoService.podeAcessar(authentication, 'EQUIPAMENTOS_USER')")
		.antMatchers("/pedido/listar", "/pedido/solicitados").access("@menuPermissaoService.podeAcessar(authentication, 'PEDIDOS_USER')")
		.and()
		.exceptionHandling().accessDeniedPage("/auth/auth-acesso-negado")
		.and()
		.formLogin().successHandler(loginSucesso)
		.loginPage("/login").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout").permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// O objeto que vai obter os detalhes do usuário
		UserDetailsService detalheDoUsuario = userDetailsServiceBean();
		// Objeto para criptografia
		BCryptPasswordEncoder criptografia = gerarCriptografia();
		
		auth.userDetailsService(detalheDoUsuario).passwordEncoder(criptografia);
	}
}
