package eredua.domeinua;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class LoginGertaera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String deskribapena;
	@Column(nullable = false)
	private Date data;
	@ManyToOne(targetEntity = Erabiltzailea.class, 
	           cascade = CascadeType.PERSIST, 
	           fetch = FetchType.EAGER)
	private Erabiltzailea erabiltzailea;
	private boolean login;

	public LoginGertaera() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Erabiltzailea getErabiltzailea() {
		return erabiltzailea;
	}

	public void setErabiltzailea(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	public boolean isLogin() {
		return login;
	}
	
	public String toString() { // LoginGertaera
		return id+"/"+deskribapena+"/"+data+"/"+erabiltzailea+"/"+login;
		}

	public void setLogin(boolean login) {
		this.login = login;
		if (login)
			this.deskribapena = erabiltzailea.getIzena() + "k ongi egin du logina";
		else
			this.deskribapena = erabiltzailea.getIzena() + " login egiten saiatu da";
	}
}