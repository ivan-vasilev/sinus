package bg.bas.iinf.sinus.hibernate.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Home {

	private static final Log log = LogFactory.getLog(Home.class);

	public static final int FLUSH_COUNT = 20;

	public static void persistInOneTransaction(EntityManager em, Collection<?> transientInstances) {
		if (transientInstances.size() == 0) {
			return;
		}

		log.debug("persisting multiple transientInstances");
		try {
			em.getTransaction().begin();
			int count = 0;
			for (Object o : transientInstances) {
				persist(em, o);
				if (++count % FLUSH_COUNT == 0) {
					em.flush();
					em.clear();
				}
			}
			em.getTransaction().commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw re;
		}
	}

	public static void persistInOneTransaction(EntityManager em, Object transientInstance) {
		log.debug("persisting " + transientInstance.getClass().getSimpleName() + " instance");
		try {
			em.getTransaction().begin();
			persist(em, transientInstance);
			em.getTransaction().commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			log.error("persist failed", re);
			throw re;
		}
	}

	/**
	 * @param em
	 *            - EntityManager
	 * @param transientInstance
	 */
	public static void persist(EntityManager em, Object transientInstance) {
		log.debug("persisting " + transientInstance.getClass().getSimpleName() + " instance");
		try {
			em.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	/**
	 * @param em
	 *            - EntityManager
	 * @param persistentInstance
	 */
	public static void removeCollectionInOneTransaction(EntityManager em, Collection<Object> persistentInstances) {
		try {
			em.getTransaction().begin();
			for (Object persistentInstance : persistentInstances) {
				remove(em, persistentInstance);
			}
			em.getTransaction().commit();
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/**
	 * @param em
	 *            - EntityManager
	 * @param persistentInstance
	 */
	public static void remove(EntityManager em, Object persistentInstance) {
		log.debug("removing " + persistentInstance.getClass().getSimpleName() + " instance");
		try {
			em.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/**
	 * @param em
	 *            - EntityManager
	 * @param persistentInstance
	 */
	public static void removeInOneTransaction(EntityManager em, Object persistentInstance) {
		try {
			em.getTransaction().begin();
			remove(em, persistentInstance);
			em.getTransaction().commit();
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	/**
	 * @param <T>
	 * @param em
	 *            - EntityManager
	 * @param detachedInstance
	 * @return
	 */
	public static <T> T merge(EntityManager em, T detachedInstance) {
		log.debug("merging " + detachedInstance.getClass().getSimpleName() + " instance");
		try {
			T result = em.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/**
	 * @param <T>
	 * @param <K>
	 * @param em
	 *            - EntityManager
	 * @param primaryKey
	 * @param entityClass
	 * @return
	 */
	public static <T, K> T findById(EntityManager em, K primaryKey, Class<T> entityClass) {
		log.debug("getting " + entityClass.getSimpleName() + " instance with id: " + primaryKey);
		try {
			T instance = em.find(entityClass, primaryKey);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static void setQueryParams(Query query, Map<String, Object> queryBindings) {
		if (queryBindings != null) {
			for (Entry<String, Object> e : queryBindings.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		}
	}
}
