package nagusia;

import javax.persistence.EntityManager;
import eredua.JPAUtil;
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

	public static void main(String[] args) {
		GertaerakSortu e = new GertaerakSortu();
		System.out.println("Gertaeren sorkuntza:");
		//e.createAndStoreLoginGertaera(1L, "Anek ondo egin du logina", new Date());
		//e.createAndStoreLoginGertaera(2L, "Nerea saiatu da login egiten", new Date());
		//e.createAndStoreLoginGertaera(3L, "Kepak ondo egin du logina", new Date());
		e.createAndStoreLoginGertaera("Anek ondo egin du logina", new Date());
		e.createAndStoreLoginGertaera("Nerea saiatu da login egiten", new Date());
		e.createAndStoreLoginGertaera("Kepak ondo egin du logina", new Date());
		System.out.println("Gertaeren zerrenda:");
		List<LoginGertaera> gertaerak = e.gertaerakZerrendatu();
		for (LoginGertaera g : gertaerak) {
			System.out.println("Id: " + g.getId() + " Deskribapena: " + g.getDeskribapena() + " Data: " + g.getData());
		}
	}
}