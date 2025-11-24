package eredua.domeinua;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Makina {
	@Id
	private int kodea;
	private String izena;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Set<Erabiltzailea> erabiltzaileak;

	public int getKodea() {
		return kodea;
	}

	public void setKodea(int kodea) {
		this.kodea = kodea;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public Set<Erabiltzailea> getErabiltzaileak() {
		return erabiltzaileak;
	}

	public void setErabiltzaileak(Set<Erabiltzailea> erabiltzaileak) {
		this.erabiltzaileak = erabiltzaileak;
	}

	public String toString() {
		return kodea + "/" + izena;
	}
}