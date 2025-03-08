package com.ecommerce.service;

import java.util.List; 
import java.util.Optional;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Producto;
import com.ecommerce.repository.IProductoRepository;

//La Anotacion me permite INYECTAR esta Clase en el Controlador, para hacer el llamado a los metodos Crud
@Service   
public class ProductoServiceImpl implements ProductoService {  //Ctrl+Spac para poner Add Metodos de implementacion

	//Para Inyectar a esta Clase un Objeto (REPOSITORIOS) q la usaremos en los metodos
	@Autowired    
	private IProductoRepository productoRepository;  //Declaramos el Objeto de tipo repository
	
	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);  //retornamos el Obj productoRepository de ProductoRepository vemos el producto q se hereda de con el metodo save 
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);  //Uso el metodo findById con el Id y me devuelve un Optional
	}

	@Override
	public void update(Producto producto) {
		//Asi actua el metodo save... Si al Objeto le manda un Null... este lo va a crear. Si lo mandamos con un ID q existe en la BDD entoces lo Actualiza
		productoRepository.save(producto);  //retornamos el Obj productoRepository de ProductoRepository vemos el producto q se hereda del metodo save
	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);// invocamos al productoRepository y usamos el metodo de Borrado por Id (deleteById)		
	}

	@Override
	public List<Producto> findAll() {
		return productoRepository.findAll();
	}

}
