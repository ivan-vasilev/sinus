package bg.bas.iinf.sinus.hibernate.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.hibernate.entity.DefaultDisplayValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;

public class DefaultDisplayValuesHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(DefaultDisplayValuesHome.class);

	public static Set<DefaultDisplayValuesPath> getPath(Iterator<IModel<OWLNamedObject>> it, OWLClass root) {
		Set<DefaultDisplayValuesPath> paths = new HashSet<DefaultDisplayValuesPath>();

		while (it.hasNext()) {
			DefaultDisplayValuesPath path = new DefaultDisplayValuesPath();
			paths.add(path);
			path.setPath(getPath(it.next(), root));
		}

		return paths;
	}

	public static String getPath(IModel<? extends OWLNamedObject> current, OWLClass root) {
		List<String> segments = new LinkedList<String>();
		boolean skip = false;
		while (current != null && !current.getObject().getIRI().equals(root.getIRI())) {
			if (!(current.getObject() instanceof OWLClass) || !skip) {
				segments.add(current.getObject().getIRI().toString());
				if (current.getObject() instanceof OWLClass) {
					skip = true;
				} else {
					skip = false;
				}
			}

			if (current instanceof OWLHierarchyNamedObjectLDM) {
				current = ((OWLHierarchyNamedObjectLDM<?>) current).getParent();
			} else {
				current = null;
			}
		}
		Collections.reverse(segments);
		return StringUtils.join(segments, ",");
	}

	public static DefaultDisplayValuesPath findByPath(EntityManager em, String path) {
		try {
			Query q = em.createQuery("from DefaultDisplayValuesPath ddvp where ddvp.path = :path").setParameter("path", path);
			List<?> result = q.getResultList();
			if (result.size() == 1) {
				return (DefaultDisplayValuesPath) result.get(0);
			}

			return null;
		} catch (RuntimeException re) {
			log.error(re);
			throw re;
		}
	}

	/**
	 * ako dadeniq klas nqma izbrani, tyrsi roditelite
	 * @param em
	 * @param defaultFilterValues
	 * @param ontologies
	 * @return
	 */
	public static List<DefaultDisplayValuesPath> getDefaultDisplayValuesPaths(EntityManager em, OWLClass owlClass, Set<OWLOntology> ontologies) {
		DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(em, owlClass.getIRI().toString());
		if (dfv != null && dfv.getDefaultDisplayValuesPaths().size() > 0) {
			return dfv.getDefaultDisplayValuesPaths();
		}

        for (OWLOntology o : ontologies) {
        	OWLReasoner r = ReasonerResolver.getReasoner(o);
        	for (OWLClass superClass : r.getSuperClasses(owlClass, false).getFlattened()) {
				dfv = DefaultFilterValuesHome.findByIRI(em, superClass.getIRI().toString());
				if (dfv != null && dfv.getDefaultDisplayValuesPaths().size() > 0) {
					return dfv.getDefaultDisplayValuesPaths();
				}
        	}
        }

		return Collections.emptyList();
	}
}
