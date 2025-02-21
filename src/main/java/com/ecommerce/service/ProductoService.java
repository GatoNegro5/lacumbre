package com.ecommerce.service;

import java.util.List;
import java.util.Optional; 

import com.ecommerce.model.Producto;

public interface ProductoService {    //Ojo en la Interface SOLO se define los Metodos CRUDs
	public Producto save(Producto producto); //Creo al Metodo de tipo Publico, va a retornar un Producto llamado "save" q recibira un Objeto de tipo Producto
	
	//Creo un Metodo que retorna un Optional(Nos da la opcion de validar si el Objeto q llama a la BDD existe o no)
	public Optional<Producto> get(Integer id) ;   //Este Metodo es de Tipo Producto, llamado get que recibira el Id que identifica al Producto
	
	public void update(Producto producto);  //Creo el Metodo de Actualizar y el parametro va el Objeto Producto
	public void delete(Integer id);
	public List<Producto> findAll();
	
}
