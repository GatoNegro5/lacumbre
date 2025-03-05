package com.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.AttributeValueExp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.model.DetalleOrden;
import com.ecommerce.model.Orden;
import com.ecommerce.model.Producto;
import com.ecommerce.service.ProductoService;

@Controller
//Cuando vaya a la Raiz del Proyecto, este controlador me va a Mapear los metodos que esten aqui  
@RequestMapping("/")
public class HomeController {

	// Creamos LOGGER para ver en Console la informacion tambien y verificarla
	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	// Variable q me permita obtener los Productos. Autowired inyecte una instacia
	// de Clase en el contenedor del framework
	@Autowired
	private ProductoService productoService;

	// Creo 2 variables: 1. para almacenar los Detalles de las Ordenes y 2. Los
	// Datos de la Orden de Compra q vienen del carrito de compras
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	Orden orden = new Orden(); // Importo el Model para q no de error

	@GetMapping("")
	public String home(Model model) { // Model lleva al informacion de los Prod a la Vista

		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) { // Model lleva inf a la Vista
		log.info("Id Producto enviado como parametro {}", id);

		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id); // Me trae una lista de tipo Optional
		producto = productoOptional.get();
		model.addAttribute("producto", producto);

		return "usuario/productoHome";
	}

	// Creo el Metodo para el Carrito de Compras - Petición q se va a mapear a una
	// Petición de tipo POS. La URL sera uan CADENA
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {   //con esto puedo ir a la BDD del Prod y anadirlo al carrito de compras 
		//creo 2 variables de Datalle y datos de la Orden
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal=0;  //guarda la suma de todos los productos escogidos
		
		
		//buscaremos el Producto a traves del Optional desde la BDD y lo vemos en Consola
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		//guardo en las variables
		producto=optionalProducto.get();
		
		//son los valores q vienen desde la VISTA 
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);     //viene el id
		
		detalles.add(detalleOrden);
		
		//vamos almacenando -el total a Pagar- de lo q va comprando y rechazando con una funcion dt q suma los totales q esten en esa lista 
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		
		//paso Toda esta Inf a la VISTA del Carrito, mediante el Model
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}

}
