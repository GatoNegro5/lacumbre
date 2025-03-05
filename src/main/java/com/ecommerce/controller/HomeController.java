package com.ecommerce.controller;

import java.util.Optional;

import javax.management.AttributeValueExp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Producto;
import com.ecommerce.service.ProductoService;

@Controller
//Cuando vaya a la Raiz del Proyecto, este controlador me va a Mapear los metodos que esten aqui  
@RequestMapping("/")   
public class HomeController {
	
	//Creamos LOGGER para ver en Console la informacion tambien y verificarla
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	
	//Variable q me permita obtener los Productos. Autowired inyecte una instacia de Clase en el contenedor del framework
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")
	public String home(Model model ) {    //Model lleva al informacion de los Prod a la Vista
		
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {   //Model lleva inf a la Vista
		log.info("Id Producto enviado como parametro {}",id);
		
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);   //Me trae una lista de tipo Optional
		producto = productoOptional.get();		
		model.addAttribute("producto", producto);
		
		return "usuario/productoHome";
	}
}
