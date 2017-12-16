package bg.bas.iinf.sinus.hibernate.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.filter.PaginationFilter;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;

public class SavedSearchesHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(SavedSearchesHome.class);

	public static SavedSearches persistOrMerge(EntityManager em, SavedSearches entity) {
		if (entity.getSavedSearchesId() == null) {
			Home.persist(em, entity);
		} else if (!em.contains(entity)) {
			return Home.merge(em, entity);
		}

		return entity;
	}

	public static SavedSearches persistOrMergeInOneTransaction(EntityManager em, SavedSearches entity) {
		SavedSearches result = null;
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

	@SuppressWarnings("unchecked")
    public static List<SavedSearches> getSavedSearches(EntityManager em, SavedSearchesFilter filter, PaginationFilter pf) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("select s from SavedSearches s");
			Map<String, Object> parameters = createFilter(query, filter);
			query.append(" order by s.savedSearchesId desc");

			Query q = em.createQuery(query.toString());

			for (Entry<String, Object> entry : parameters.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}

			if (pf != null) {
				pf.setLimits(q);
			}

			return q.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

    public static Long getSavedSearchesCount(EntityManager em, SavedSearchesFilter filter) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("select count(s) from SavedSearches s");
			Map<String, Object> parameters = createFilter(query, filter);

			Query q = em.createQuery(query.toString());

			Home.setQueryParams(q, parameters);

            return (Long) q.getSingleResult();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

    private static Map<String, Object> createFilter(StringBuilder query, SavedSearchesFilter filter) {
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	query.append(" where 1 = 1");
    	if (filter.getUserId() != null) {
    		query.append(" and s.users.usersId = :userId");
    		parameters.put("userId", filter.getUserId());
    	}

    	if (filter.getNameFilter() != null && !StringUtils.isEmpty(filter.getNameFilter().getStringLike())) {
    		query.append(" and s.humanReadable like :humanReadable");
    		parameters.put("humanReadable", filter.getNameFilter().getClause());
    	}

    	if (!StringUtils.isEmpty(filter.getObjectUri())) {
    		query.append(" and s.objectUri like :objectUri");
    		parameters.put("objectUri", filter.getObjectUri());
    	}

    	if (filter.getTag() != null && !StringUtils.isEmpty(filter.getTag().getStringLike())) {
    		query.append(" and s.tag like :tag");
    		parameters.put("tag", filter.getTag().getClause());
    	}

    	return parameters;
    }

    @SuppressWarnings("unchecked")
	public static List<String> getSavedSearchesLike(EntityManager em, StringFilter filter, PaginationFilter paging) {
		log.debug("getting SavedSearches like filter");
		try {
			Query query = em.createQuery("select s.humanReadable from SavedSearches s where upper(s.humanReadable) like upper(:param) order by s.humanReadable").setParameter("param", filter.getClause());
			if (paging != null) {
				paging.setLimits(query);
			}

			List<String> savedSearches = query.getResultList();

			log.debug("get successful");

			return savedSearches;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

    @SuppressWarnings("unchecked")
    public static List<String> getSavedSearchesTags(EntityManager em, SavedSearchesFilter filter, PaginationFilter paging) {
    	log.debug("getting SavedSearches like filter");
    	try {
			StringBuilder query = new StringBuilder();
			query.append("select distinct s.tag from SavedSearches s");

			Map<String, Object> parameters = createFilter(query, filter);
			query.append(" and s.tag != null");
			Query q = em.createQuery(query.toString());

			Home.setQueryParams(q, parameters);

    		if (paging != null) {
    			paging.setLimits(q);
    		}

    		List<String> savedSearches = q.getResultList();

    		log.debug("get successful");

    		return savedSearches;
    	} catch (RuntimeException re) {
    		log.error("get failed", re);
    		throw re;
    	}
    }
}
