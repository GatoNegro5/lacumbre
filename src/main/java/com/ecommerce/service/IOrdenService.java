package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.model.Orden;
import com.ecommerce.model.Usuario;

public interface IOrdenService {
	//Para secuenciar un numero de Orden automatico, esto para q se implemente en OrdenServiceImpl
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);  //Para buscar el usuario por id 
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);  //Para buscar el usuario por id sin Optional
}
