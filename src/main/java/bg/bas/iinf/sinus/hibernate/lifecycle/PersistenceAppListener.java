package bg.bas.iinf.sinus.hibernate.lifecycle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import bg.bas.iinf.sinus.cache.CacheFactory;

/**
 * zatvarq EntityManagerFactory, ipolzva se pri syzdavane/unishtojavane na cqloto prilojenie
 * @author hok
 *
 */
public class PersistenceAppListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent evt) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		PersistenceManager.getInstance().closeEntityManagerFactory();
		CacheFactory.getCache().getCacheManager().shutdown();
		CacheFactory.getGeneralCache().getCacheManager().shutdown();
		CacheFactory.getReasonerCache().getCacheManager().shutdown();
	}
}
