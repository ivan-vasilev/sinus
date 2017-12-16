package bg.bas.iinf.sinus.hibernate.lifecycle;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * zatvarq entityManager-a v kraq na vseki request
 * @author hok
 *
 */
public class PersistenceAppRequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		ScopedEntityManagerFactory.closeEntityManager();
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
	}
}
