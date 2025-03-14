package com.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.model.Usuario;
import com.ecommerce.service.IUsuarioService;

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
	
	//grabamos lo ingresado
	@PostMapping("/save")
	//este metodo traera los datos del Usuario desde el Cliente Mapeados y vinculado a este Objeto usuario
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario); //puedo revisar en Consola lo ingresado en Registro en la Web
		usuario.setTipo("USER");   //defino tipo de usuario
		usuarioService.save(usuario);
		
		return "redirect:/";   //graba y va a la Raiz
	}
}
