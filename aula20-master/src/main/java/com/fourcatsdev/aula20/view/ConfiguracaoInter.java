package com.fourcatsdev.aula20.view;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.*;

@Configuration
public class ConfiguracaoInter implements WebMvcConfigurer {
	@Bean
    public MessageSource messageSource() { 
        ReloadableResourceBundleMessageSource messageSource = 
			new ReloadableResourceBundleMessageSource(); 
        messageSource.setBasename("classpath:message");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() { 
        CookieLocaleResolver clr = new CookieLocaleResolver(); 
        clr.setDefaultLocale(Locale.US); 
        return clr;
	}


    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor(); 
        interceptor.setParamName("locale"); 
        registry.addInterceptor(interceptor); 
    }
	
}
