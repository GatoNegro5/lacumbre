package com.ecommerce.model;

public class Producto {
	private Integer id;
	private String nombre;
	private String descripcion;
	Private String imagen;
	private double precio;
	private int cantidad;
	
	// Contructor sin parametros
	public Producto() {
		// TODO Auto-generated constructor stub
	}
	
	//Contructor con parametros
	public Producto(Integer id, java.lang.String nombre, java.lang.String descripcion, Private string, double precio,
			int cantidad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		String = string;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	//Setter & Getter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Private getString() {
		return String;
	}

	public void setString(Private string) {
		String = string;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	//Me retorna todos los campos de la clase como 1 solo String
	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", cantidad=" + cantidad + "]";
	}
	
	
		
}
