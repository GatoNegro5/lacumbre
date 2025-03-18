package com.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Orden;
import com.ecommerce.model.Usuario;
import com.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	//Autowired indica q lo q sigue se inyecte esta Clase
	@Autowired
	private IOrdenRepository ordenRepository;  //en este Objeto almacenaremos los CRUDs
	
	@Override
	public Orden save(Orden orden) {		
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {		
		return ordenRepository.findAll();
	}

	public String generarNumeroOrden() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero()))); //recorro con forEach y lambda(->) la Lista Integer
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();  //Obtengo el mayor numero que este en la List
			numero++;
		}
		//ahora lo paso a uan cadena de 10 digitos
		if (numero < 10) {
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado="000000"+String.valueOf(numero);
		}		
		
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}
}
