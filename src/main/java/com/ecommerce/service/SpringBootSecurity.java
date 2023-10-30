package com.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity {

	  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests(authorize -> authorize
	                .requestMatchers("/administrador/**").hasRole("ADMIN")
	                .requestMatchers("/productos/**").hasRole("ADMIN")
	                .requestMatchers("/usuario/login").permitAll()
	                .requestMatchers().authenticated()
	            )
	            .formLogin()
	            .loginPage("/usuario/login")
	            .defaultSuccessUrl("/usuario/acceder")
	            .and()
	            .rememberMe(remember -> remember
	                .rememberMeServices(rememberMeServices())
	            )
	            .csrf(csrf -> csrf
	                .csrfTokenRepository(csrfTokenRepository())
	            );

	        return http.build();
	    }

	@Bean
	public RememberMeServices rememberMeServices() {
		return new TokenBasedRememberMeServices("ClavemyKey4217347", userDetailsService());
	}

	@Bean
	public UserDetailServiceImpl userDetailsService() {
		// Implement your UserDetailsService
		// Return a custom UserDetailsService
		return new UserDetailServiceImpl(); // Replace with your custom implementation
	}

	@Bean
	public CsrfTokenRepository csrfTokenRepository() {
		CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
		return csrfTokenRepository;
	}
}