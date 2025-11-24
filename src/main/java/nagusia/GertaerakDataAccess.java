package nagusia;

import eredua.JPAUtil;
import eredua.domeinua.Erabiltzailea;
import eredua.domeinua.LoginGertaera;
import eredua.domeinua.Makina;
import eredua.domeinua.Pertsona;
import jakarta.validation.ConstraintViolationException;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;

import java.util.*;

public class GertaerakDataAccess {
	public GertaerakDataAccess() {
	}

	public Erabiltzailea createAndStoreErabiltzailea(String izena, String pasahitza, String mota) {
		EntityManager em = JPAUtil.getEntityManager();
		Erabiltzailea e = new Erabiltzailea();
		try {
			em.getTransaction().begin();
			e.setIzena(izena);
			e.setPasahitza(pasahitza);
			e.setMota(mota);
			em.persist(e);
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
		return e;
	}

	public LoginGertaera createAndStoreLoginGertaera(String erabiltzailea, boolean login, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		LoginGertaera e = new LoginGertaera();
		try {
			em.getTransaction().begin();
			e.setErabiltzailea((Erabiltzailea) em.find(Erabiltzailea.class, erabiltzailea));
			e.setLogin(login);
			e.setData(data);
			// if (data!=null) e.setData(data);
			// else throw new Exception("data falta da");
			em.persist(e);
			em.getTransaction().commit();
		} catch (PersistenceException ex) {
			e = null;
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (ex.getCause() instanceof ConstraintViolationException
					|| ex.getCause() instanceof PropertyValueException) {
				System.out.println("Errorea: data falta da");
			}
		} catch (Exception ex) {
			System.out.println("Errorea: erabiltzailea ez da existitzen: " + ex.toString());
			e = null;
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		} finally {
			em.close();
		}
		return e;
	}

	public List<LoginGertaera> getLoginGertaerak() {
		EntityManager em = JPAUtil.getEntityManager();
		List<LoginGertaera> result = new ArrayList<LoginGertaera>();
		try {
			em.getTransaction().begin();
			result = em.createQuery("from LoginGertaera", LoginGertaera.class).getResultList();
			// System.out.println("getLoginGertaerak() : "+result);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<Erabiltzailea> getErabiltzaileak() {
		EntityManager em = JPAUtil.getEntityManager();
		List<Erabiltzailea> result = new ArrayList<Erabiltzailea>();
		try {
			em.getTransaction().begin();
			result = em.createQuery("from Erabiltzailea", Erabiltzailea.class).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv1(String erabiltzaileaIzena) {
		EntityManager em = JPAUtil.getEntityManager();
		List<LoginGertaera> result = new ArrayList<LoginGertaera>();
		try {
			em.getTransaction().begin();
			TypedQuery<LoginGertaera> q = em.createQuery(
					"select lg from LoginGertaera lg inner join lg.erabiltzailea e where e.izena= :erabiltzaileaIzena",
					LoginGertaera.class);
			q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
			result = q.getResultList();
			System.out.println("result: " + result);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv2(String erabiltzaileaIzena) {
		EntityManager em = JPAUtil.getEntityManager();
		List<LoginGertaera> result = new ArrayList<LoginGertaera>();
		try {
			em.getTransaction().begin();
			TypedQuery<LoginGertaera> q = em.createQuery(
					"select lg from LoginGertaera lg where lg.erabiltzailea.izena=:erabiltzaileaIzena",
					LoginGertaera.class);
			q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
			result = q.getResultList();
			System.out.println("result: " + result);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return result;
	}

	public List<LoginGertaera> getLoginGertaerakv3(String erabiltzaileaIzena) {
		EntityManager em = JPAUtil.getEntityManager();
		List<LoginGertaera> result = new ArrayList<LoginGertaera>();
		try {
			em.getTransaction().begin();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LoginGertaera> query = cb.createQuery(LoginGertaera.class);
			Root<LoginGertaera> loginGertaera = query.from(LoginGertaera.class);
			Join<LoginGertaera, Erabiltzailea> erabiltzailea = loginGertaera.join("erabiltzailea");
			query.select(loginGertaera).where(cb.equal(erabiltzailea.get("izena"), erabiltzaileaIzena));
			result = em.createQuery(query).getResultList();
			System.out.println("result: " + result);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		return result;
	}

	public boolean deleteErabiltzailea(String erabiltzailea) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Erabiltzailea e = (Erabiltzailea) em.find(Erabiltzailea.class, erabiltzailea);
			// Query q = em.createQuery("delete from LoginGertaera where erabiltzailea =
			// :erab");
			// q.setParameter("erab", e);
			// q.executeUpdate();
			em.remove(e);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				System.out.println("Errorea: " + e.toString());
				return false;
			}
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		return true;
	}

	public Erabiltzailea createAndStoreErabiltzaileaLoginGertaeraBatekin(String izena, String pasahitza, String mota,
			boolean login, Date data) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Erabiltzailea e = new Erabiltzailea();
			e.setIzena(izena);
			e.setPasahitza(pasahitza);
			e.setMota(mota);

			LoginGertaera lg = new LoginGertaera();
			lg.setErabiltzailea(e);
			lg.setLogin(login);
			lg.setData(data);

			// e.getGertaerak().add(lg);
			// Set<LoginGertaera> gertaerak = new HashSet<>();
			// gertaerak.add(lg);
			// e.setGertaerak(gertaerak);

			// HashSet<LoginGertaera> gs = new HashSet<>();
			// gs.add(lg);
			// e.setGertaerak(gs);

			// em.persist(e);
			em.persist(lg);

			em.getTransaction().commit();
			return e;
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}

	public Erabiltzailea getErabiltzailea(String erabiltzaileaIzena) {
		EntityManager em = JPAUtil.getEntityManager();
		Erabiltzailea result = null;
		try {
			em.getTransaction().begin();
			TypedQuery<Erabiltzailea> q = em.createQuery(
					"select e from Erabiltzailea e where e.izena= :erabiltzaileaIzena", Erabiltzailea.class);
			q.setParameter("erabiltzaileaIzena", erabiltzaileaIzena);
			result = q.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Errorea erabiltzailea errekuperatzean: " + e.getMessage());
		} finally {
			em.close();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		GertaerakDataAccess e = new GertaerakDataAccess();
		// System.out.println("Gertaeren sorkuntza:"); //
		// e.createAndStoreErabiltzailea("Ane", "125", "ikaslea");
		// e.createAndStoreLoginGertaera("Ane", true, new Date());
		// e.createAndStoreLoginGertaera("Ane", false, new Date());
		// e.createAndStoreErabiltzailea("Kepa", "126", "ikaslea");
		// e.createAndStoreLoginGertaera("Kepa", true, new Date());
		// e.createAndStoreLoginGertaera("Kepa", false, new Date());
		// List<Erabiltzailea> er = e.getErabiltzaileak();
		// System.out.println("3.1 => Erabiltzaileak:" + er);
		// List<LoginGertaera> lg = e.getLoginGertaerak();
		// System.out.println("3.1 => Login Gertaerak: " + lg);
		// Erabiltzailea erab = lg.get(0).getErabiltzailea();
		// lg.get(0) login gertaeratik erabiltzailea lortzen da: .getErabiltzailea()
		// System.out.println("3.2 => " + erab);
		// List<LoginGertaera> lg1 = e.getLoginGertaerakv1(erab.getIzena());
		// System.out.println("3.3.1 => " + erab.getIzena() + "ren Login Gertaerak: " +
		// lg1);

		// List<LoginGertaera> lg2 = e.getLoginGertaerakv2(erab.getIzena());
		// System.out.println("3.3.2 => " + erab.getIzena() + "ren Login Gertaerak: " +
		// lg2);
		// List<LoginGertaera> lg3 = e.getLoginGertaerakv3(erab.getIzena());
		// System.out.println("3.3.3 => " + erab.getIzena() + "ren Login Gertaerak: " +
		// lg3);
		// System.out.println("3.4 => " + erab.getGertaerak());
		// LoginGertaera lgAne = e.createAndStoreLoginGertaera("Ane", true, null);
		// System.out.println("4.1 => " + lgAne);
		// e.createAndStoreErabiltzailea("Nekane", "127", "ikaslea");
		// e.createAndStoreLoginGertaera("Nekane", true, new Date());
		// System.out.println("4.2.1 => " + e.getLoginGertaerak());
		// boolean res = e.deleteErabiltzailea("Nekane");
		// System.out.println("4.2.2 => " + e.getLoginGertaerak());
		// erab = e.createAndStoreErabiltzaileaLoginGertaeraBatekin("Peru", "128",
		// "ikaslea", true, new Date());
		// System.out.println("4.3.1 => " + e.getErabiltzaileak());
		// System.out.println("4.3.2 => Erabiltzailea: " + erab + " Bere gertaerak: " +
		// erab.getGertaerak());
		// System.out.println("4.3.3 => " + e.getLoginGertaerak());
		// System.out.println("4.4 => Erabiltzailea: " + erab); // Peru izango da
		// System.out.println("4.4.1 => " + erab.getGertaerak()); // erab objektuak ez
		// du login gertaerarik
		// erab = e.getErabiltzailea("Peru");
		// System.out.println("4.4.2 => " + erab.getGertaerak()); // Orain bai:
		// datu-basetik ekarri da eta!!
		// EntityManager em = JPAUtil.getEntityManager();
		// LoginGertaera lg5;
		// try {
		// em.getTransaction().begin();
		// System.out.println("5.1 => find(LoginGertaera.class,1L) => ");
		// lg5 = em.find(LoginGertaera.class, 1L);
		// EntityGraph<?> graph = em.getEntityGraph("LoginGertaera.erabiltzailearekin");
		// Map<String, Object> propietateak = new HashMap<>();
		// propietateak.put("javax.persistence.fetchgraph", graph);
		// System.out.println("5.1 => find(LoginGertaera.class,1L,propietateak) => ");
		// lg5 = em.find(LoginGertaera.class, 1L, propietateak); System.out.println("5.2
		// => getErabiltzailea().getMota() => ");
		// System.out.println("5.1 => SELECT e FROM LoginGertaera e LEFT JOIN FETCH
		// e.erabiltzailea WHERE e.id = :id");
		// lg5 = em.createQuery("SELECT e FROM LoginGertaera e LEFT JOIN FETCH
		// e.erabiltzailea WHERE e.id = :id",LoginGertaera.class)
		// .setParameter("id", 1L)
		// .getSingleResult();
		// System.out.println("5.1 => SELECT DISTINCT e FROM LoginGertaera e LEFT JOIN
		// FETCH e.erabiltzailea u LEFT JOIN FETCH u.gertaerak WHERE e.id = :id");
		// lg5 = em.createQuery("SELECT DISTINCT e FROM LoginGertaera e " +
		// "LEFT JOIN FETCH e.erabiltzailea u " +
		// "LEFT JOIN FETCH u.gertaerak " +
		// "WHERE e.id = :id",
		// LoginGertaera.class)
		// .setParameter("id", 1L)
		// .getSingleResult();
		// System.out.println(lg5.getErabiltzailea().getMota());
		// em.getTransaction().commit();
		// } catch (Exception ex) {
		// if (em.getTransaction().isActive()) {
		// em.getTransaction().rollback();
		// }
		// System.out.println("Errorea: " + ex.getMessage());
		// } finally {
		// em.close();
		// }
		/*
		 * EntityManager em = JPAUtil.getEntityManager(); try {
		 * em.getTransaction().begin(); Erabiltzailea e1=new Erabiltzailea();
		 * e1.setIzena("Mikel"); e1.setPasahitza("125"); e1.setMota("ikaslea");
		 * Erabiltzailea e2=new Erabiltzailea(); e2.setIzena("Maider");
		 * e2.setPasahitza("126"); e2.setMota("ikaslea");
		 * 
		 * Makina m1=new Makina(); m1.setKodea(1); m1.setIzena("Etxea");
		 * 
		 * Makina m2=new Makina(); m2.setKodea(2); m2.setIzena("Lana");
		 * 
		 * Set<Makina> ms=new HashSet<Makina>(); ms.add(m1); ms.add(m2);
		 * 
		 * Set<Erabiltzailea> es=new HashSet<Erabiltzailea>(); es.add(e1); es.add(e2);
		 * 
		 * e1.setMakinak(ms); e2.setMakinak(ms); m1.setErabiltzaileak(es);
		 * m2.setErabiltzaileak(es);
		 * 
		 * em.persist(e1); em.persist(e2); em.persist(m1); em.persist(m2);
		 * 
		 * em.getTransaction().commit();
		 * System.out.println("M1:<"+m1+","+m1.getErabiltzaileak()+">");
		 * System.out.println("M2:<"+m2+","+m2.getErabiltzaileak()+">"); em =
		 * JPAUtil.getEntityManager(); em.getTransaction().begin();
		 * e1=(Erabiltzailea)em.find(Erabiltzailea.class,"Mikel");
		 * System.out.println("E1:<"+e1+","+e1.getMakinak()+">");
		 * e2=(Erabiltzailea)em.find(Erabiltzailea.class,"Maider");
		 * System.out.println("E2:<"+e2+","+e2.getMakinak()+">");
		 * em.getTransaction().commit(); } catch (Exception ex) { if
		 * (em.getTransaction().isActive()) { em.getTransaction().rollback(); }
		 * System.out.println("Error: " + ex.getMessage()); } finally { em.close(); }
		 */
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Erabiltzailea e3 = new Erabiltzailea();
			e3.setIzena("Koldo");
			e3.setPasahitza("125");
			e3.setMota("ikaslea");
			Pertsona p1 = new Pertsona();
			p1.setTelefonoa("943112233");
			e3.setPertsona(p1);
			p1.setErabiltzailea(e3);
			em.persist(e3);
			em.persist(p1);
			em.getTransaction().commit();
			System.out.println("P:<" + e3.getPertsona() + "> E:<" + e3.getPertsona().getErabiltzailea() + ">");
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Errorea : " + ex.getMessage());
		} finally {
			em.close();
		}
	}
}