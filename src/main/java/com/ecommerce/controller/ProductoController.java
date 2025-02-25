package com.ecommerce.controller;

import java.io.IOException;
import java.util.Optional; 

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import com.ecommerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
//Mapeados por Productos
@RequestMapping("/productos")
public class ProductoController {
	
	//Creo la variable Logger para hacer pruebas de grabar Producto desde el Backend
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	//Creo la variable para acceder a todos los metodos CRUDs
	@Autowired
	private ProductoService productoService;
	
	//Creo un Obj tipo upload, q la inyectaremos con Autowired a la Clase Producto
	@Autowired
	private UploadFileService upload;
	
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
	
	//Creo metodo q Mapea con el nombre de save la Informacion desde el boton Guardar los Planes de Servicio 
	@PostMapping("/save")
	//Parametros: recibe un Obj Prod de la Clase y Recibe con la Notacion name=img del create.html nombre de la Imagen
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {    
		//primero hago el test en consola
		LOGGER.info("Este es el objeto producto {}", producto);  //{} Va dentro el Objeto que viene del ToString
		
		//Creo un Usuario en SQL... ahora le llamo a ese usuario creando un Objeto q solo indique el ID del que cree en SQL
		Usuario u = new Usuario(1,"","","","","","",""); 
		producto.setUsuario(u);
		
		//Guardo el nombre de la imagen en la BDD, pero si es cargada por 1era vez (id=1)
		if(producto.getId()==null) {  //condicion cuando se crea un producto
			String nombreImagen = upload.saveImage(file); //Esta variable file viene desde el @RequestParam con el nombre de la imagen
			producto.setImagen(nombreImagen);  //aqui se guardara el Obj.Prod q viene de ProdController"save" en la BDD
		}		
		
		//Y ahora guardo el contenido de la WEB de Productos en la BDD
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	//Lleva a la nueva pagina Edit... Mapea toda la Informacion del Registro pero con el Id del Producto Seleccionado
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {      //declaro una variable para recibir el parametro - PathVariable sirve para recibir un parametro
		Producto producto = new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);     //Optional devuelve Objeto cuando hacemos la busqueda de un Objeto
		producto = optionalProducto.get();     //Aqui me llaga el producto q mande a buscar
		
		//Testeamos q busque
		LOGGER.info("Este es el objeto producto buscado {}", producto);
		model.addAttribute("producto", producto);     //Nos va a enviar a la vista todo el Obj
		return "productos/edit";  //Lleva a Pag.Edit
	}
	
	//Al metodo va a responder una peticion de tipo POST con la terminacion URL update
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p = new Producto();   //1.definimos un Obj.tipo Prod.
		p = productoService.get(producto.getId()).get();  //2.Obtenemos la Imagen que tenia del ProdService
		
		
		//cuando se modifica un Prod y pero va la misma Imagen
		if(file.isEmpty()) {  //Modifico el Prod... pero le cargo la misma Imagen q tenia
			
			producto.setImagen(p.getImagen());   //3.Nuevamente lo pasamos al Prod. q estamos editando
			
		}else {
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			//cuando se modifica un Prod y Si se cambia Imagen por Otra
			String nombreImagen = upload.saveImage(file); //Esta variable file viene desde el @RequestParam con el nombre de la imagen
			producto.setImagen(nombreImagen);  //aqui se guardara el Obj.Prod q viene de ProdController"save" en la BDD
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);		
		return "redirect:/productos";
	}
	
	//Este metodo responde a una peticion GetMapping con un id que me permitirá Mapear el Reg a Eliminar
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {      //declaro una variable para recibir el parametro - PathVariable sirve para recibir un parametro
		Producto p = new Producto();
		p=productoService.get(id).get();
		
		//eliminar cuando no sea la imagen por defecto
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
}
