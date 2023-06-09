package titoli;

import java.time.LocalTime;
import java.util.List;
import javax.persistence.*;
import JPA_util.JpaUtil;
import negozi.PuntiVendita;
import negozi.negoziDAO;
import tratte.Tratta;
import tratte.TratteDAO;
import java.time.LocalDate;


public class TitoliDAO {
	
	static EntityManager em = JpaUtil.getEmf().createEntityManager();
	
	public static void main(String[] args) {
		
		
		
		
		
		try {
			
			/*List<Titolo> listaTitoli = findAllTitoli();
			Abbonamento A1 = new Abbonamento();
			A1.setData_emissione(LocalDate.of(2023, 4, 4));
			A1.setDurata(TipoAbbonamento.MENSILE);
            A1.setData_inizio(LocalDate.of(2020, 2, 7), A1.getDurata());
			System.out.println(A1.toString());
			addTitolo(A1);
			
			Biglietto B1 = new Biglietto();
			B1.setData_emissione(LocalDate.of(2023, 4, 4));
			System.out.println(B1.toString());
			addTitolo(B1);
			
			Abbonamento A2 = new Abbonamento();
            A2.setData_emissione(LocalDate.of(2023, 3, 28));
            A2.setDurata(TipoAbbonamento.SETTIMANALE);
            A2.setData_inizio(LocalDate.of(2020, 2, 7), A2.getDurata());
            System.out.println(A2.toString());
            addTitolo(A2);

            Biglietto B2 = new Biglietto();
            B2.setData_emissione(LocalDate.of(2023, 4, 3));
            System.out.println(B2.toString());
            addTitolo(B2);
            
            Abbonamento A3 = new Abbonamento();
            A3.setData_emissione(LocalDate.of(2023, 2, 28));
            A3.setDurata(TipoAbbonamento.SETTIMANALE);
            A3.setData_inizio(LocalDate.of(2020, 2, 7), A3.getDurata());
            System.out.println(A3.toString());
            addTitolo(A3);

            Biglietto B3 = new Biglietto();
            B3.setData_emissione(LocalDate.of(2023, 5, 3));
            System.out.println(B3.toString());
            addTitolo(B3);
            
            Abbonamento A4 = new Abbonamento();
            A4.setData_emissione(LocalDate.of(2020, 2, 7));
            A4.setDurata(TipoAbbonamento.MENSILE);
            A4.setData_inizio(LocalDate.of(2020, 2, 7), A4.getDurata());
            System.out.println(A4.toString());
            addTitolo(A4);

            Biglietto B4 = new Biglietto();
            B4.setData_emissione(LocalDate.of(2022,5,6));
            System.out.println(B4.toString());
            addTitolo(B4);
            
           B1.setVidimato(true);
           System.out.println(B1.toString());
			
           Titolo letto = serchByNum(2l);
          listaTitoli =  findAllTitoli();
          listaTitoli.forEach(el -> System.out.println(el));*/
			
			//calcolaBiglietti(1l);
			//calcolaAbbonamenti(1l);
            
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			em.close();
		}
		
	}
	
	public static List<Titolo> findAllTitoli() {
	    Query q = em.createNamedQuery("titoli.findAll"); 
	    return q.getResultList(); 
	}

	public static void addTitolo(Titolo e) {
	    em.getTransaction().begin(); 
	    em.persist(e); 
	    em.getTransaction().commit(); 
	    System.out.println("Nuovo titolo aggiunto!");
	}

	public static void dellTitolo(Titolo e) {
	    em.getTransaction().begin(); 
	    em.remove(e); 
	    em.getTransaction().commit();
	    System.out.println("Titolo eliminato!"); 
	}

	public static Titolo serchByNum(Long id) {
	    em.getTransaction().begin(); 

	    TypedQuery<Titolo> query = em.createQuery("SELECT t FROM Titolo t WHERE t.id = :id", Titolo.class);
	    query.setParameter("id", id);
	    Titolo resultp = query.getSingleResult(); 

	    em.getTransaction().commit(); 

	    if (resultp != null) {
	        System.out.println(resultp.toString()); 
	    } else {
	        System.out.println("Nessun titolo di viaggio trovato con questo numero " + id); 
	    }
	    return resultp; 
	}

	public static void validaBiglietto(Long id) {
	    Biglietto tit = (Biglietto) serchByNum(id); 
	    tit.setValido(false); 
	    em.getTransaction().begin(); 
	    em.merge(tit); 
	    em.getTransaction().commit(); 
	}

	
	public static List<Titolo> findtitoloDaNegozio(Long id) {
		Query q = em.createNamedQuery("FindTitoliDaNegozio");
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	public static void calcolaBiglietti(Long id) {
	    em.getTransaction().begin(); 

	    TypedQuery<Long> query = (TypedQuery<Long>) em.createQuery("SELECT COUNT(*) FROM Titolo t WHERE id_puntivendita = :id AND tipologia_biglietto LIKE 'Biglietto'");
	    query.setParameter("id", id);
	    Long totBiglietti = query.getSingleResult(); 

	    em.getTransaction().commit(); 

	    System.out.println(totBiglietti); 
	    caricaTotBiglietti(id, totBiglietti); 
	}

	public static void caricaTotBiglietti(long id, long n) {
	    PuntiVendita t = negoziDAO.findNegozioByID(id); 
	    t.setBiglietti_emessi(n); 
	    em.getTransaction().begin(); 
	    em.merge(t); 
	    em.getTransaction().commit(); 
	}

	public static void calcolaAbbonamenti(Long id) {
	    em.getTransaction().begin(); 

	    TypedQuery<Long> query = (TypedQuery<Long>) em.createQuery("SELECT COUNT(*) FROM Titolo t WHERE id_puntivendita = :id AND tipologia_biglietto LIKE 'Abbonamento'");
	    query.setParameter("id", id);
	    Long totAbbonamenti = query.getSingleResult(); 

	    em.getTransaction().commit(); 

	    System.out.println(totAbbonamenti); 
	    caricaTotAbbonamenti(id, totAbbonamenti); 
	}

	public static void caricaTotAbbonamenti(long id, long n) {
	    PuntiVendita t = negoziDAO.findNegozioByID(id); 
	    t.setAbbonamenti_emessi(n); 
	    em.getTransaction().begin(); 
	    em.merge(t); 
	    em.getTransaction().commit(); 
	}

}
