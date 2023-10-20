package com.ecommerce.service;


import java.util.List;

import com.ecommerce.model.Orden;
import com.ecommerce.model.Usuario;


public interface IOrdenService {
	List<Orden> findAll();
	Orden save(Orden orden);	
	String generarNumeroOrden();
	List <Orden> findByUsuario(Usuario usuario);
}
