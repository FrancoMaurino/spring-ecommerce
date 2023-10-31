package com.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity implements WebMvcConfigurer {

	@Autowired
	private UserDetailsService userDetailService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
             
         http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> {
                request.requestMatchers("/usuario/login").permitAll();
                request.requestMatchers("/administrador/**").hasAuthority( "ADMIN");
                request.requestMatchers("/productos/**").hasAuthority( "ADMIN");
            })
            .formLogin(formLogin -> formLogin
                    .loginPage("/usuario/login")
                    .permitAll()
                    .defaultSuccessUrl("/usuario/acceder"));               
                
         http.cors(withDefaults());
     return http.build();   
         
    }
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*
	 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 * {
	 * auth.userDetailsService(userDetailsService()).passwordEncoder(getEnecoder());
	 * }
	 */
	

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
	
}