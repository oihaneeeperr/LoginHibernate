package nagusia;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import eredua.JPAUtil;
import eredua.domeinua.Erabiltzailea;
import eredua.domeinua.LoginGertaera;
import java.util.*;

public class GertaerakSortu {
	public GertaerakSortu() {
	}

	private void createAndStoreLoginGertaera(Long id, String deskribapena, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			LoginGertaera e = new LoginGertaera();
			e.setId(id);
			e.setDeskribapena(deskribapena);
			e.setData(data);
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private void createAndStoreLoginGertaera1(String deskribapena, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			LoginGertaera e = new LoginGertaera();
			e.setDeskribapena(deskribapena);
			e.setData(data);
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private void createAndStoreLoginGertaera(String deskribapena, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			LoginGertaera e = new LoginGertaera();
			e.setDeskribapena(deskribapena);
			e.setData(data);
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private List<LoginGertaera> gertaerakZerrendatu() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<LoginGertaera> result = em.createQuery("from LoginGertaera", LoginGertaera.class).getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private void createAndStoreErabiltzailea(String izena, String pasahitza, String mota) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Erabiltzailea u = new Erabiltzailea();
			u.setIzena(izena);
			u.setPasahitza(pasahitza);
			u.setMota(mota);
			em.persist(u);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private void createAndStoreLoginGertaera(String erabil, boolean login, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			TypedQuery<Erabiltzailea> q = em.createQuery("SELECT e FROM Erabiltzailea e WHERE e.izena = :erabiltzailea",
					Erabiltzailea.class);
			q.setParameter("erabiltzailea", erabil);
			List<Erabiltzailea> result = q.getResultList();
			if (!result.isEmpty()) {
				LoginGertaera e = new LoginGertaera();
				e.setErabiltzailea(result.get(0));
				e.setLogin(login);
				e.setData(data);
				em.persist(e);
				em.getTransaction().commit();
			} else {
				System.out.println("Errorea: erabiltzailea ez da existitzen: " + erabil);
				em.getTransaction().rollback();
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public void printObjMemDB(String azalpena, Erabiltzailea e) {
		System.out.print("\tMem:<" + e + "> DB:<" + GertaerakBerreskuratuJDBC.getErabiltzaileaJDBC(e) + "> =>");
		System.out.println(azalpena);
	}

	public static void main(String[] args) {
		GertaerakSortu e = new GertaerakSortu();
		// System.out.println("Gertaeren sorkuntza:");
		// e.createAndStoreLoginGertaera(1L, "Anek ondo egin du logina", new Date());
		// e.createAndStoreLoginGertaera(2L, "Nerea saiatu da login egiten", new
		// Date());
		// e.createAndStoreLoginGertaera(3L, "Kepak ondo egin du logina", new Date());
		// e.createAndStoreLoginGertaera("Anek ondo egin du logina", new Date());
		// e.createAndStoreLoginGertaera("Nerea saiatu da login egiten", new Date());
		// e.createAndStoreLoginGertaera("Kepak ondo egin du logina", new Date());
		// System.out.println("Gertaeren zerrenda:");
		//
		// List<LoginGertaera> gertaerak = e.gertaerakZerrendatu();
		// for (LoginGertaera g : gertaerak) {
		// System.out.println("Id: " + g.getId() + " Deskribapena: " +
		// g.getDeskribapena() + " Data: " + g.getData());
		// }
		e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
		e.createAndStoreLoginGertaera("Ane", true, new Date());
		e.createAndStoreLoginGertaera("Ane", false, new Date());
		System.out.println("Gertaeren zerrenda:");
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<LoginGertaera> result = em.createQuery("from LoginGertaera", LoginGertaera.class).getResultList();

			for (int i = 0; i < result.size(); i++) {
				LoginGertaera ev = (LoginGertaera) result.get(i);
				System.out.println("Id: " + ev.getId() + " Deskribapena: " + ev.getDeskribapena() + " Data: "
						+ ev.getData() + " Login: " + ev.isLogin());
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
		System.out.println("======================");
		System.out.println("Objektuen Bizi-Zikloa:");
		System.out.println("======================");
		Erabiltzailea u = new Erabiltzailea();
		u.setIzena("Nerea");
		u.setPasahitza("1234");
		u.setMota("irakaslea");
		System.out.println("new => TRANSIENT");
		e.printObjMemDB("Nerea bakarrik dago memorian eta ez DBan", u);

		EntityManager em1 = JPAUtil.getEntityManager();
		try {
			em1.getTransaction().begin();
			em1.persist(u);
			System.out.println("persist => MANAGED");
			e.printObjMemDB("Nerea oraindik ez dago DBan commit egin ez delako", u);
			u.setPasahitza("1235");
			e.printObjMemDB("u.setPasahitza(\"1235\") exekutatu da, baina ez commit-a. Beraz, Nerea ez dago DBan", u);
			em1.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemDB("Commit egin da, DBan ikusten dira aldaketak", u);
			u.setPasahitza("1236");
			e.printObjMemDB(
					"u.setPasahitza(\"1236\") exekutatu da, baina objektua ez dago DBarekin konektatuta (detached)", u);
		} catch (Exception ex) {
			if (em1.getTransaction().isActive()) {
				em1.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em1.close();
		}

		EntityManager em2 = JPAUtil.getEntityManager();
		try {
			em2.getTransaction().begin();
			em2.persist(u);
			em2.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println(
					"save => ERROREA: objektua 'detached' dagoenez, 'save' horrekin objektu berdina berriz ere sartzen saiatzen da, eta gako-errorea sortuko da");
			if (em2.getTransaction().isActive()) {
				em2.getTransaction().rollback();
			}
		} finally {
			em2.close();
		}

		EntityManager em3 = JPAUtil.getEntityManager();
		try {
			em3.getTransaction().begin();
			em3.merge(u);
			System.out.println("merge => MANAGED");
			e.printObjMemDB(
					"orain objetua MANAGED egoeran dago, baina pasahitz berria ikusteko, lehenago kommit egin behar da",
					u);
			em3.getTransaction().commit();
			System.out.println("commit => DETACHED");
			e.printObjMemDB("\tcommit egin da, eta pasahitz berria ikusten da datu-basean", u);
		} catch (Exception ex) {
			if (em3.getTransaction().isActive()) {
				em3.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em3.close();
		}

		u.setPasahitza("1237");
		e.printObjMemDB(
				"u.setPasahitza(\"1237\") ondo exekutatu da, baina objektua ez dago konektatuta datu-basearekin (detached)",
				u);

		EntityManager em4 = JPAUtil.getEntityManager();
		try {
			em4.getTransaction().begin();
			u = em4.find(Erabiltzailea.class, u.getIzena());
			System.out.println("find => MANAGED");
			e.printObjMemDB(
					"Berriz errekuperatu da erabiltzailea datu-basetik, baina pasahitzaren aldaketa galdu egin da", u);
			em4.remove(u);
			System.out.println("remove => REMOVED");
			e.printObjMemDB("commit egin arte ez da objekua datu-basetik ezabatuko ", u);
			em4.getTransaction().commit();
			System.out.println("commit => DETACHED");
			e.printObjMemDB("commit egin da, objektua ez dago datu-basean baina bai memorian. ", u);
		} catch (Exception ex) {
			if (em4.getTransaction().isActive()) {
				em4.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em4.close();
		}
	}
}