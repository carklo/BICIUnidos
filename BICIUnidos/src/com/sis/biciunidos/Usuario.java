package com.sis.biciunidos;

public class Usuario 
{
	private int amigos;
	private String nombreUser;
	private String nombre;
	private String lastlatLong;
	private long lastTime;
	private double PhoneNumber;
	private String email;
	private double numKilometros;
	private double ritmo;
	private double pedalazos;
	private int caravanas;
	private int caloriasTotales;
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
	public Usuario(int am, String nomb, String nameUser, String latlong, long last, double num, double ri, double pe, String em, double kil,int car, int cal)
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

	public int getAmigos() {
		return amigos;
	}

	public void setAmigos(int amigos) {
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
	public double getNumKilom() {
		return numKilometros;
	}

	/**
	 * @param numKilom the numKilom to set
	 */
	public void setNumKilom(double numKilom) {
		this.numKilometros = numKilom;
	}

	/**
	 * @return the ritmo
	 */
	public double getRitmo() {
		return ritmo;
	}

	/**
	 * @param ritmo the ritmo to set
	 */
	public void setRitmo(double ritmo) {
		this.ritmo = ritmo;
	}

	/**
	 * @return the pedalazos
	 */
	public double getPedalazos() {
		return pedalazos;
	}

	/**
	 * @param pedalazos the pedalazos to set
	 */
	public void setPedalazos(double pedalazos) {
		this.pedalazos = pedalazos;
	}

	/**
	 * @return the phoneNumber
	 */
	public double getPhoneNumber() {
		return PhoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(double phoneNumber) {
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
	public int getCaravanas() {
		return caravanas;
	}

	/**
	 * @param caravanas the caravanas to set
	 */
	public void setCaravanas(int caravanas) {
		this.caravanas = caravanas;
	}

	/**
	 * @return the caloriasTotales
	 */
	public int getCaloriasTotales() {
		return caloriasTotales;
	}

	/**
	 * @param caloriasTotales the caloriasTotales to set
	 */
	public void setCaloriasTotales(int caloriasTotales) {
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
