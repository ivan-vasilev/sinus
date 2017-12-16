package bg.bas.iinf.sinus.hibernate.lifecycle;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * syzdava EntityManager i go vryzva kym ThreadLocal obekt
 * @author hok
 *
 */
public class ScopedEntityManagerFactory {

	private static final Log log = LogFactory.getLog(PersistenceManager.class);

	private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<EntityManager>();

	private ScopedEntityManagerFactory() {
	}

	/**
	 * "Production" - tova se polzva ot cqlata funkcionalnost
	 * @return
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = threadLocal.get();
		if (em == null || !em.isOpen()) {
			em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
			em.setFlushMode(FlushModeType.COMMIT); // po-efektivno, ima vyzmojnost za greshki
			threadLocal.set(em);
			log.debug("EntityManager created");
		}

		return em;
	}

	public static void closeEntityManager() {
		EntityManager em = threadLocal.get();
		if (em != null) {
			threadLocal.set(null);
	        em.close();
	        log.debug("EntityManager closed");
        }
	}
}
