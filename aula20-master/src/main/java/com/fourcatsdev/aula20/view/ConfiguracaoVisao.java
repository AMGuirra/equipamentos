package com.fourcatsdev.aula20.view;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ConfiguracaoVisao implements WebMvcConfigurer {
	
	public void addViewControllers(@SuppressWarnings("null") ViewControllerRegistry registry) {
		registry.addViewController("/auth/auth-acesso-negado").setViewName("/auth/auth-acesso-negado");
	}

}
