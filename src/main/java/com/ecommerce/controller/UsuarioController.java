package com.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Usuario;
import com.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired
	private IUsuarioService usuarioService;  //Esto me permitira Inyectar a este Controlador las operaciones CRUD desde UsuarioServiceImpl.java
	
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
	
	
}
