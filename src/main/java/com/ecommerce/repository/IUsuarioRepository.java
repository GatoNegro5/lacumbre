package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Usuario;
import java.util.List;


@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer>{     //se Mapeara mediante el Integer
	Optional<Usuario> findByEmail(String email);   //Busco un Usuario q tenga este metodo, q coincida con el parametro email
}
