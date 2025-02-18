package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//Mapeados por Productos
@RequestMapping("/productos")
public class ProductoController {
	
	//Lo q haremos es crear un Metodo q redireccione hacia la vista q es "show.html"
	@GetMapping("")
	public String show() {
		return "productos/show";
	}
}
