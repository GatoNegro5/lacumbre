package com.ecommerce.service;

import java.util.Optional;

import com.ecommerce.model.Usuario;

public interface IUsuarioService {
	Optional<Usuario> findById(Integer id);
	//defino un metodo Guardar para USUARIO- para q se implemente en el UsuarioServiceImpl
	Usuario save(Usuario usuario);
	
	Optional<Usuario> findByEmail(String email);  //busco al usuario x Mail, igual para q se implemente en UusarioServiceImpl
}
