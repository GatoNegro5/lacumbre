package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Producto;

@Repository   //Para q se inyecte en el SERVICE como Artefacto de Spring
public interface ProductoRepository extends JpaRepository<Producto, Integer>{  //asi la interfaz sabra donde tiene q hacer el Crud - Integer es el ID de cada Clase
	
}
