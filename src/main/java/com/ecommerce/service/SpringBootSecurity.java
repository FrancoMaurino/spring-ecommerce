package com.ecommerce.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ecommerce.service.jwt.JwtFilter;

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
	
	@Autowired
	private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
             
         http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request -> {
                request.requestMatchers("usuario/acceder").permitAll();// Rutas públicas sin requerir autenticación.
                request.requestMatchers("/administrador/**").hasAuthority( "ADMIN");// Requiere autorización "ADMIN" para la ruta .
                request.requestMatchers("/productos/**").hasAuthority( "ADMIN");
            }) .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);;
                 
                
         http.cors(withDefaults());
     return http.build();   
         
    }
 
	/*
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
	 * throws Exception {
	 * 
	 * http.csrf(AbstractHttpConfigurer::disable)
	 * .cors(AbstractHttpConfigurer::disable) .formLogin(formLogin -> formLogin
	 * .loginPage("/usuario/login") .permitAll()
	 * .defaultSuccessUrl("/usuario/acceder")) .authorizeHttpRequests(request -> {
	 * 
	 * request.requestMatchers("/administrador/**").hasAuthority( "ADMIN");
	 * request.requestMatchers("/productos/**").hasAuthority( "ADMIN");
	 * 
	 * });
	 * 
	 * http.cors(withDefaults()); return http.build();
	 * 
	 * }
	 */
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
	    return authenticationConfiguration.getAuthenticationManager();
	}
	

		
	
}