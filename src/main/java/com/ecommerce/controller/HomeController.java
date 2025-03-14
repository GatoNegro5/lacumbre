package com.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.ecommerce.model.Usuario;
import com.ecommerce.service.IDetalleOrdenService;
import com.ecommerce.service.IOrdenService;
import com.ecommerce.service.IUsuarioService;
import com.ecommerce.service.ProductoService;

@Controller
//Cuando vaya a la Raiz del Proyecto, este controlador me va a Mapear los metodos que esten aqui  (BACKEND)
@RequestMapping("/")
public class HomeController {

	// Creamos LOGGER para ver en Console la informacion tambien y verificarla
	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	// Autowired inyecte una instacia de Clase en el contenedor del framework
	@Autowired
	private ProductoService productoService;  // Variable q me permita obtener los Productos.
	
	@Autowired
	private IUsuarioService usuarioService;
	
	//Creo 2 Objetos q contendra los datos de la Orden para guardarla/persistirla en la BDD
	@Autowired
	private IOrdenService ordenService;
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	

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

	// Creo Método q agrega el Prod escogido para COMPRARLO y lo lista en la Página del Carrito de Compras 
	// Esta Petición q se va a mapear a una Petición de tipo POS. La URL será una CADENA q coincide con la del carrito.html
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
		
		//Validamos q el Prod no se añada 2 veces
		Integer idProducto = producto.getId();
		
		//Los datos q están en el Carrito se encuentran en "detalles" donde vamos a preguntar con Lambda "->" si el id del New Prod ya se encuentra ahí, responderá True/False  
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);  //uso Método AnyMatch(alguna Coincidencia) y su predicado(va la busqueda)
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		//Suma -el total a Pagar- de lo q vamos comprando y borrando Prod con una funcion dt q suma los totales q esten en esa lista 
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		
		//paso Toda esta Inf a la VISTA del Carrito, mediante el Model
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Quitar un Prod del Carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {     //1.Decimo q va a mappearse a un entero id - Model envia la Inf de nuevo a Vista
		
		//lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		for(DetalleOrden detalleOrden: detalles) {
			if(detalleOrden.getProducto().getId() != id) {   //Hago un for nuevo descartando al señalado, no borro sino lo descarto
				ordenesNueva.add(detalleOrden);
			}
		}
		
		//poner la nueva lista de Productos restantes
		detalles = ordenesNueva;
		
		//Suma -el total a Pagar- de lo q queda de las compras con una funcion dt q suma los totales q esten en esa lista 
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
				
		//paso Toda esta Inf a la VISTA del Carrito, mediante el Model
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
				
		return "usuario/carrito";
	}

	//Activamos la Opcion CARRITO del Menu de la Pagina
	@GetMapping("/getCart")
	public String getCart(Model model) {
		//detalles(Lista de Productos) orden(valor a pagar) son Globales y sobreviven a toda la ejecucion de la aplicacion, solo paso la Inf a la VISTA del Carrito con Model
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Activamos el Boton: VER ORDEN del Carrito
	@GetMapping("/order")
	public String order(Model model) {
		
		Usuario usuario = usuarioService.findById(1).get();   //Tomo 1 de la BDD hasta q agreguemos el ingreso de Usuarios... uso get para q retorne el Optional
		model.addAttribute("usuario", usuario); //Traigo el Atributo Usuario a traves del Model
		
		model.addAttribute("cart", detalles);  //detalles del Prod escogidos
		model.addAttribute("orden", orden);    //detalles de la Orden de los Prod Escogidos y Rechazados
		
		return "usuario/resumenorden";          //regresa al html
	}
	
		
	//Guardamos la Orden del Carrito en la BDD
	@GetMapping("/saveOrder")
	public String saveOrder() {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//ponemos al usuario loggeado
		Usuario usuario = usuarioService.findById(1).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);  //guardamos la Orden
		//Ahora guardamos los DETALLES de la Orden. 1.Creo Un Obj DetalleOrden llamado dt y va a leer los datos de la LISTA "detalles" con for
		for (DetalleOrden dt:detalles) {
			dt.setOrden(orden);    //pasamos la primera linea de la lista de la Orden
			detalleOrdenService.save(dt);  //La Guardamos en la BDD
		}
		//Limpiamos los detos de la Lista de Detalles y de Orden
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/"; //volvemos a la HOME de la Web
		
	}
	
	//Funcionalidad BUSCAR
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		log.info("Nombre del Plan: {}", nombre);  //imprimo el nombre del Prod en Consola si quisiera verificarle q lo toma
		//filtro el Texto de BUSCAR,todos los Prod como Stream, con funcion anonima de flecha p- variable q recorrera la lista y p tendra el nombre de c/Prod 
		List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList()); //Colecto todo en Lista  
		//Ahora lo paso a la Vista con model... usando la Implementacion
		model.addAttribute("productos", productos);
		return "usuario/home";
	}
}
	
