package bg.bas.iinf.sinus.hibernate.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bg.bas.iinf.sinus.hibernate.entity.UsersConfirmation;

@Stateless
public class UsersConfirmationHome {

	private static final Log log = LogFactory.getLog(UsersConfirmationHome.class);

	public static UsersConfirmation findByConfirmKey(EntityManager em, String confirmKey) {
		log.debug("getting UsersConfirmation instance with edit key: " + confirmKey);
		try {
			Query query = em.createQuery("from UsersConfirmation uc where uc.confirmKey = :confirmKey").setParameter("confirmKey", confirmKey);
			UsersConfirmation instance = (UsersConfirmation) query.getSingleResult();
			log.debug("get successful");
			return instance;
		} catch (NoResultException ne) {
			log.error("get failed", ne);
			throw ne;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
