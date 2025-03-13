package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Orden;

public interface IOrdenService {
	//Para secuenciar un numero de Orden automatico, esto para q se implemente en OrdenServiceImpl
	List<Orden> findAll();
	
	Orden save (Orden orden);
	String generarNumeroOrden();
}
