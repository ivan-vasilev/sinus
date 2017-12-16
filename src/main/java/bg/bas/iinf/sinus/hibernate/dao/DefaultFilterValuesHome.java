package bg.bas.iinf.sinus.hibernate.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;

public class DefaultFilterValuesHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(DefaultFilterValuesHome.class);

	public static DefaultFilterValues findByIRI(EntityManager em, String iri) {
		try {
			Query q = em.createQuery("from DefaultFilterValues dfv where dfv.iri = :iri").setParameter("iri", iri);
			List<?> result = q.getResultList();
			if (result.size() == 1) {
				return (DefaultFilterValues) result.get(0);
			}

			return null;
		} catch (RuntimeException re) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			log.error(re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
    public static List<DefaultFilterValues> getDefaultFilterValues(EntityManager em, Set<OWLOntology> ontologies) {
		if (ontologies == null || ontologies.size() == 0) {
			return Collections.<DefaultFilterValues>emptyList();
		}

		try {
			Set<String> ontologyIds = new HashSet<String>();
			for (OWLOntology o : ontologies) {
				ontologyIds.add(o.getOntologyID().toString());
			}

			Query q = em.createQuery("from DefaultFilterValues dfv where dfv.ontologies.id in (:ontology_ids)").setParameter("ontology_ids", ontologyIds);

			return q.getResultList();
		} catch (RuntimeException re) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			log.error(re);
			throw re;
		}
	}
}
