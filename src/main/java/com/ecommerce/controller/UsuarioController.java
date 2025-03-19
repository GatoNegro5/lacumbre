package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Orden;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.IOrdenService;
import com.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;  //Esto Controla las operaciones CRUD del UsuarioServiceImpl.java
	
	@Autowired
	private IOrdenService ordenService;  //Para controlar las Ordenes q se vera segun si el Usuario se ha logueado o no
	
	//accedemos a una nueva Web /usuario/registro mediante GetMapping
	@GetMapping("/registro")
	public String create() {		
		return "usuario/registro";
	}
	
	//grabamos el registro ingresado
	@PostMapping("/save")
	//este metodo traera los datos del Usuario desde el Cliente Mapeados y vinculado a este Objeto usuario
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario); //puedo revisar en Consola lo ingresado en Registro en la Web
		usuario.setTipo("USER");   //defino tipo de usuario
		usuarioService.save(usuario);		
		return "redirect:/";   //graba y va a la Raiz
	}
	
	//accedemos a una nueva Web /usuario/login mediante GetMapping
	@GetMapping("/login")
	public String login() {			
		return "usuario/login";
	}
	
	//Verificamos el Login ingresado
	@PostMapping("/acceder")
	//este metodo traera los datos del Login desde el Cliente Mapeados y vinculado a este Objeto usuario
	public String acceder(Usuario usuario, HttpSession session) {     //Obj session estara presente TODA la APlicacion
		logger.info("Usuario Acessos: {}", usuario); //puedo revisar en Consola lo ingresado en Login en la Web
		
		//Traigo el Usuario encontrado con el metodo findByEmail con Optional
		Optional<Usuario> user=usuarioService.findByEmail(usuario.getEmail());
		//logger.info("Usuario Encontrado con finByemail de BDD: {}", user.get());
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		}else {
			logger.info("Usuario no existe, ingrese nueevamente o Registrese");
		}
		return "redirect:/";   //graba y va a la Raiz
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {			
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		//Hago una busqueda con el Obj usuarioService creado arriba del id del Usuario para pasarlo a la busqueda de Ordenes
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);   //Listo las Ordenes del Usuario Logueado llamando al Metodo implementado de la Clase OrdenServiceImpl
		
		
		//Ahora le vamos a pasar a la Vista de compras.html, una Lista llamada "ordenes" 
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String datalleCompra(@PathVariable Integer id, Model model, HttpSession session) {	//La Notacion PathVariable mapea el parametro q viene de la URL
		logger.info("Id de la orden: {}", id);    //Reviso el id logueado en consola
		Optional<Orden> orden = ordenService.findById(id);  //Creo el Obj con la Orden de un usuario con id
		
		//JPA trae la Orden junto a los detalles, y con Model manda a la vista
		model.addAttribute("detalles", orden.get().getDetalle());
		
		//Paso los datos a las Vistas de quien esta en la Session
		model.addAttribute("sesion", session.getAttribute("idusuario"));  //paso la sesion a la Vista
		return "usuario/detallecompra";  //va a detallecompra.html
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}
}
