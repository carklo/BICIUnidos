package com.sis.biciunidos;

public class Usuario 
{
	private long amigos;
	private String nombreUser;
	private String nombre;
	private String lastlatLong;
	private long lastTime;
	private long PhoneNumber;
	private String email;
	private long numKilometros;
	private long ritmo;
	private long pedalazos;
	private long caravanas;
	private long caloriasTotales;
	private String keyU;
	
	/**
	 * 
	 * @param am
	 * @param nomb
	 * @param latLoc
	 * @param last
	 * @param num
	 * @param ri
	 * @param pe
	 */
	public Usuario(long am, String nomb, String nameUser, String latlong, long last, long num, long ri, long pe, String em, long kil,long car, long cal)
	{
		setAmigos(am);
		setNombreUser(nameUser);
		setNombre(nomb);
		setLastTime(last);
		setLastlatLong(latlong);
		setPhoneNumber(num);
		setRitmo(ri);
		setPedalazos(pe);
		setEmail(em);
		setNumKilom(kil);
		setCaravanas(car);
		setCaloriasTotales(cal);
	}

	public long getAmigos() {
		return amigos;
	}

	public void setAmigos(long amigos) {
		this.amigos = amigos;
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
	 * @return the lastTime
	 */
	public long getLastTime() {
		return lastTime;
	}

	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * @return the numKilom
	 */
	public long getNumKilom() {
		return numKilometros;
	}

	/**
	 * @param numKilom the numKilom to set
	 */
	public void setNumKilom(long numKilom) {
		this.numKilometros = numKilom;
	}

	/**
	 * @return the ritmo
	 */
	public long getRitmo() {
		return ritmo;
	}

	/**
	 * @param ritmo the ritmo to set
	 */
	public void setRitmo(long ritmo) {
		this.ritmo = ritmo;
	}

	/**
	 * @return the pedalazos
	 */
	public long getPedalazos() {
		return pedalazos;
	}

	/**
	 * @param pedalazos the pedalazos to set
	 */
	public void setPedalazos(long pedalazos) {
		this.pedalazos = pedalazos;
	}

	/**
	 * @return the phoneNumber
	 */
	public long getPhoneNumber() {
		return PhoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(long phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the nombreUser
	 */
	public String getNombreUser() {
		return nombreUser;
	}

	/**
	 * @param nombreUser the nombreUser to set
	 */
	public void setNombreUser(String nombreUser) {
		this.nombreUser = nombreUser;
	}

	/**
	 * @return the lastlatLong
	 */
	public String getLastlatLong() {
		return lastlatLong;
	}

	/**
	 * @param lastlatLong the lastlatLong to set
	 */
	public void setLastlatLong(String lastlatLong) {
		this.lastlatLong = lastlatLong;
	}

	/**
	 * @return the caravanas
	 */
	public long getCaravanas() {
		return caravanas;
	}

	/**
	 * @param caravanas2 the caravanas to set
	 */
	public void setCaravanas(long caravanas2) {
		this.caravanas = caravanas2;
	}

	/**
	 * @return the caloriasTotales
	 */
	public long getCaloriasTotales() {
		return caloriasTotales;
	}

	/**
	 * @param caloriasTotales the caloriasTotales to set
	 */
	public void setCaloriasTotales(long caloriasTotales) {
		this.caloriasTotales = caloriasTotales;
	}

	/**
	 * @return the keyU
	 */
	public String getKeyU() {
		return keyU;
	}

	/**
	 * @param keyU the keyU to set
	 */
	public void setKeyU(String keyU) {
		this.keyU = keyU;
	}
}
