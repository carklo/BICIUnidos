package com.sis.biciunidos;

import com.google.android.gms.maps.model.LatLng;

public class Marcador 
{
	private LatLng posicion;
	private String tipo;
	
	public Marcador(LatLng pos, int tip)
	{
		posicion = pos;
		if(tip == Constantes.MARCADOR_INICIO)
		{
			tipo = "Inicio";
		}
		else if(tip == Constantes.MARCADOR_FINAL)
		{
			tipo = "Fin";
		}
		else
		{
			tipo = "Intermedio";
		}
	}

	public LatLng getPosicion() {
		return posicion;
	}

	public void setPosicion(LatLng posicion) {
		this.posicion = posicion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
