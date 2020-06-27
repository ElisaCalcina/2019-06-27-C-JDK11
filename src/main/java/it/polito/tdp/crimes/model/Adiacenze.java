package it.polito.tdp.crimes.model;

public class Adiacenze {

	String v1;
	String v2;
	Double peso;
	
	public Adiacenze(String v1, String v2, Double peso) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return  v1 + " " + v2 + " " + peso;
	}
	
	
}