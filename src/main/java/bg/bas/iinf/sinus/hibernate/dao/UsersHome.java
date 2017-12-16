package bg.bas.iinf.sinus.hibernate.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.filter.UsersFilter;

@Stateless
public class UsersHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(UsersHome.class);

	public static Users persistOrMerge(EntityManager em, Users entity) {
		if (entity.getUsersId() == null) {
			Home.persist(em, entity);
		} else if (!em.contains(entity)) {
			return Home.merge(em, entity);
		}

		return entity;
	}

	public static Users persistOrMergeInOneTransaction(EntityManager em, Users entity) {
		Users result = null;
		try {
			em.getTransaction().begin();
			result = persistOrMerge(em, entity);
			em.getTransaction().commit();
		} catch (RuntimeException re) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			log.error(re);
			throw re;
		}

		return result;
	}

	public static Users findById(EntityManager em, Integer id) {
		return Home.findById(em, id, Users.class);
	}

	@SuppressWarnings("unchecked")
	public static Users findByUsernameOrEmailAndPasswod(EntityManager em, String auth, byte[] password) {
		log.debug("getting Users instance with username: " + auth + " and password: " + password);
		try {

			// http://opensource.atlassian.com/projects/hibernate/browse/HHH-5292 - tova e buga, dano da se fixne
			Query query = em.createQuery("from Users s where (upper(s.name) like upper(:auth) or upper(s.email) like upper(:auth)) and :passwordHash = s.passwordHash").setParameter("auth", auth)
					.setParameter("passwordHash", password);

			List<Users> users = query.getResultList();
			if (users.size() > 0) {
				log.debug("get successful");
				return users.get(0);
			}
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static Users findByUsernameAndEmail(EntityManager em, String username, String eMail) {
		log.debug("getting Users instance with username: " + username + " and email: " + eMail);
		try {
			Query query = em.createQuery("from Users u where upper(u.name) like upper(:username) and upper(u.email) like upper(:mail)").setParameter("username", username).setParameter("mail", eMail);

			@SuppressWarnings("unchecked")
            List<Users> users = query.getResultList();
			if (users.size() > 0) {
				log.debug("get successful");
				return users.get(0);
			}

			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static Users findByUsernameOrEmail(EntityManager em, String username) {
		log.debug("getting Users instance with username or email: " + username);
		try {
			Query query = em.createQuery("from Users u where upper(u.name) like upper(:username) or upper(u.email) like upper(:username)").setParameter("username", username);

			@SuppressWarnings("unchecked")
            List<Users> users = query.getResultList();
			if (users.size() > 0) {
				log.debug("get successful");
				return users.get(0);
			}
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static boolean userNameExists(EntityManager em, String name) {
		log.debug("Checking if user '" + name + "' exists");
		try {
			Query query = em.createQuery("select count(*) from Users s where upper(s.name) like upper(:param)").setParameter("param", name);
			Long count = (Long) query.getSingleResult();
			log.debug("get successful");

			return count > 0;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static boolean userEmailExists(EntityManager em, String email) {
		log.debug("Checking if user email '" + email + "' exists");
		try {
			Query query = em.createQuery("select count(*) from Users s where upper(s.email) like upper(:param)").setParameter("param", email);
			Long count = (Long) query.getSingleResult();
			log.debug("get successful");

			return count > 0;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public static List<String> getUserNames(EntityManager em, UsersFilter filter) {
		log.debug("Getting users");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select u.name from Users u");
			Map<String, Object> queryBindings = new HashMap<String, Object>();
			createQueryWhere(filter, sb, queryBindings);
			sb.append(" order by u.name");

			Query query = em.createQuery(sb.toString());

			Home.setQueryParams(query, queryBindings);

			if (filter.getPaging() != null) {
				filter.getPaging().setLimits(query);
			}

			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
    public static List<Users> getUsers(EntityManager em, UsersFilter filter) {
		log.debug("Getting users");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select u from Users u");
			Map<String, Object> queryBindings = new HashMap<String, Object>();
			createQueryWhere(filter, sb, queryBindings);
			sb.append(" order by u.name");

			Query query = em.createQuery(sb.toString());

			Home.setQueryParams(query, queryBindings);

			if (filter.getPaging() != null) {
				filter.getPaging().setLimits(query);
			}

			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static Long getUsersCount(EntityManager em, UsersFilter filter) {
		log.debug("Getting users");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select count(*) from Users u");
			Map<String, Object> queryBindings = new HashMap<String, Object>();
			createQueryWhere(filter, sb, queryBindings);
			Query query = em.createQuery(sb.toString());

			Home.setQueryParams(query, queryBindings);

			return (Long) query.getSingleResult();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	private static void createQueryWhere(UsersFilter filter, StringBuilder sb, Map<String, Object> queryBindings) {
		sb.append(" where 1 = 1");
		if (filter.getName() != null) {
			sb.append("and u.name like :name");
			queryBindings.put("name", filter.getName().getClause());
		}
	}
}
