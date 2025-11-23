package eredua.domeinua;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Erabiltzailea {
	@Id
	private String izena;
	private String pasahitza;
	private String mota;

	public Erabiltzailea() {
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}
	
	public String toString() { // Erabiltzailea
		return izena+"/"+pasahitza+"/"+mota;
		}
}