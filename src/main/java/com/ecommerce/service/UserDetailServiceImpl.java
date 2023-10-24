package com.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;
	
		
	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	HttpSession session;
	
	private Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		LOGGER.info("Esto es el userName");
		Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {
			LOGGER.info("esto es el id del usuario: {}", optionalUser.get().getId());
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario =  optionalUser.get();
			return User.builder().username(usuario.getNombre()).password(getEncoder().encode(usuario.getPassword())).roles(usuario.getTipo()).build();
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
	}
	
}	
