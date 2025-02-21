package com.ecommerce.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;

@Controller
//Mapeados por Productos
@RequestMapping("/productos")
public class ProductoController {
	
	//Creo la variable Logger para hacer pruebas de grabar Producto desde el Backend
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	//Creo la variable para acceder a todos los metodos CRUDs
	@Autowired
	private ProductoService productoService;
	
	
	//Lo q haremos es crear un Metodo q redireccione hacia la vista q es "show.html"
	@GetMapping("")
	public String show(Model model) {   //Le pasamos un Objeto Model como parametro - Model lleva informacion de Prod desde el Backend hacia la Vista Show
		model.addAttribute("productos", productoService.findAll());  //1er parametro lo q recibo (prod), 2do parametro la lista de Obj q tiene la Informacion
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() { 		
		return "productos/create";
	}
	
	//Creo metodo q Mapea con el nombre de save la Informacion desde el boton Guardar Planes de Servicio 
	@PostMapping("/save")
	public String save(Producto producto) {    //recibira un Objeto Producto de la Clase
		//primero hago el test en consola
		LOGGER.info("Este es el objeto producto {}", producto);  //{} Va dentro el Objeto que viene del ToString
		
		//Creo un Usuario en SQL... ahora le llamo a ese usuario creando un Objeto q solo indique el ID del que cree en SQL
		Usuario u = new Usuario(1,"","","","","","",""); 
		producto.setUsuario(u);
		
		//Y ahora guardo el contenido de la WEB de Productos
		productoService.save(producto);
		return "redirect:/productos";
	}
}
