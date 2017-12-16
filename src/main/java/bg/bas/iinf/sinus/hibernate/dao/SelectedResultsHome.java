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

import bg.bas.iinf.sinus.hibernate.entity.SelectedResults;
import bg.bas.iinf.sinus.hibernate.filter.PaginationFilter;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;

public class SelectedResultsHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(SelectedResultsHome.class);

	@SuppressWarnings("unchecked")
    public static List<SelectedResults> getSelectedResults(EntityManager em, SelectedResultsFilter filter, PaginationFilter pf) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("select s from SelectedResults s");
			Map<String, Object> parameters = createFilter(query, filter);
			query.append(" order by s.selectedResultsId desc");

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

    public static Long getSelectedResultsCount(EntityManager em, SelectedResultsFilter filter) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("select count(s) from SelectedResults s");
			Map<String, Object> parameters = createFilter(query, filter);

			Query q = em.createQuery(query.toString());

			parameters.entrySet();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}

            return (Long) q.getSingleResult();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

    private static Map<String, Object> createFilter(StringBuilder query, SelectedResultsFilter filter) {
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	query.append(" where 1 = 1");
    	if (filter.getUserId() != null) {
    		query.append(" and s.users.usersId = :userId");
    		parameters.put("userId", filter.getUserId());
    	}

    	if (!StringUtils.isEmpty(filter.getClassIRI())) {
    		query.append(" and s.classIri like :classIri");
    		parameters.put("classIri", filter.getClassIRI());
    	}

    	if (filter.getObjectIRIs() != null && filter.getObjectIRIs().size() > 0) {
    		query.append(" and s.objectIri in (:objectIris)");
    		parameters.put("objectIris", filter.getObjectIRIs());
    	}

    	if (filter.getTag() != null && !StringUtils.isEmpty(filter.getTag().getStringLike())) {
    		query.append(" and s.savedSearches.tag like :tag");
    		parameters.put("tag", filter.getTag().getClause());
    	}

    	return parameters;
    }
}
