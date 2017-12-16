package bg.bas.iinf.sinus.hibernate.lifecycle;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * updavlqva factory-to
 * @author hok
 *
 */
public class PersistenceManager {

	private static final Log log = LogFactory.getLog(PersistenceManager.class);

	private static final PersistenceManager singleton = new PersistenceManager();

	protected EntityManagerFactory emf;

	public static final String PERSISTENCE_UNIT = "sinusPersistenceUnit";

	public static PersistenceManager getInstance() {
		return singleton;
	}

	private PersistenceManager() {
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			createEntityManagerFactory(PERSISTENCE_UNIT);
		}

		return emf;
	}

	public void closeEntityManagerFactory() {
		if (emf != null) {
			emf.close();
			emf = null;
			log.debug("Persistence finished at " + new java.util.Date());
		}
	}

	protected void createEntityManagerFactory(String persistenceUnit) {
		this.emf = Persistence.createEntityManagerFactory(persistenceUnit);
		log.debug("Persistence unit \"" + persistenceUnit + "\" started at " + new java.util.Date());
	}
}
