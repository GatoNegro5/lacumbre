package com.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	private String folder="images//"; //tendra la ubicacion raiz de las imagenes de nuestro proyecto - La BDD solo almacenará "el nombre" de la imagen x escalabilidad
			
	//creo 1er Metodo Subo Imagen
	public String saveImage(MultipartFile file) throws IOException {  //tipo de variable para que tenga una imagen como parametro
		if(!file.isEmpty()) {
			//la imagen la paso a bits en un Array para q pueda pasarse del Cliente al Servidor
			byte [] bytes = file.getBytes();
			//pongo la ubicacion(/path) donde quiero q se guarde
			Path path = Paths.get(folder+file.getOriginalFilename());
			//paso la imagen
			Files.write(path, bytes);
			//Retorno el nombre de mi imagen q he subido
			return file.getOriginalFilename()
		}
		return "default.jpg";
	}
	
	//2do Metodo Elimina Imagen si elimino un Producto
	public void deleteImage(String nombre) {    //Parametro es el nombre de la Imagen
		//ruta de la Imagen a borrar y el nombre
		String ruta= "images//";
		File file = new File(ruta+nombre);
		file.delete();
	}
}
