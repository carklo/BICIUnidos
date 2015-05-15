package com.sis.biciunidos;

import com.google.android.gms.maps.model.LatLng;

public class PlaceMark 
{
	private String nombre;
	private String descripcion;
	private String coordenadas;
	
	/**
	 * Empty constructor
	 */
	public PlaceMark()
	{
	}
	
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the coordenadas
	 */
	public String getCoordenadas() {
		return coordenadas;
	}
	/**
	 * @param coordenadas the coordenadas to set
	 */
	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}
	
}