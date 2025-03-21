package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Usuario;
import com.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
	
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> findById(Integer id) {
		// Me retorna un Usuario de Tipo OPTIONAL
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		//guarda en la BDD
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {		
		return usuarioRepository.findByEmail(email);  //implementado el Metodo findByEmail
	}
	
}
