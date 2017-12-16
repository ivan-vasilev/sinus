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
import bg.bas.iinf.sinus.hibernate.entity.DefaultAnnotateValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;

public class DefaultAnnotateValuesHome implements Serializable {

	private static final long serialVersionUID = -5215707284374318463L;

	private static final Log log = LogFactory.getLog(DefaultAnnotateValuesHome.class);

	public static Set<DefaultAnnotateValuesPath> getPath(Iterator<IModel<OWLNamedObject>> it, OWLClass root) {
		Set<DefaultAnnotateValuesPath> paths = new HashSet<DefaultAnnotateValuesPath>();

		while (it.hasNext()) {
			DefaultAnnotateValuesPath path = new DefaultAnnotateValuesPath();
			paths.add(path);
			path.setPath(getPath(it.next(), root));
		}

		return paths;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getPath(IModel<OWLNamedObject> current, OWLClass root) {
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
				current = ((OWLHierarchyNamedObjectLDM) current).getParent();
			} else {
				current = null;
			}
		}
		Collections.reverse(segments);
		return StringUtils.join(segments, ",");
	}

	public static DefaultAnnotateValuesPath findByPath(EntityManager em, String path) {
		try {
			Query q = em.createQuery("from DefaultAnnotateValuesPath davp where davp.path = :path").setParameter("path", path);
			List<?> result = q.getResultList();
			if (result.size() == 1) {
				return (DefaultAnnotateValuesPath) result.get(0);
			}

			return null;
		} catch (RuntimeException re) {
			log.error(re);
			throw re;
		}
	}

	/**
	 * @param em
	 * @param model
	 * @param root
	 * @return - is added (false if already exists)
	 */
	public static boolean addPath(EntityManager em, IModel<OWLNamedObject> model, OWLClass root) {
		try {
			DefaultAnnotateValuesPath path = DefaultAnnotateValuesHome.findByPath(em, getPath(model, root));
			if (path == null) {
				DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(em, root.getIRI().toString());
				path = new DefaultAnnotateValuesPath();
				path.setPath(getPath(model, root));
				path.setDefaultFilterValues(dfv);
				dfv.getDefaultAnnotateValuesPaths().add(path);
				Home.persistInOneTransaction(em, dfv);
				return true;
			}

			return false;
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
	public static List<DefaultAnnotateValuesPath> getDefaultAnnotateValuesPaths(EntityManager em, OWLClass owlClass, Set<OWLOntology> ontologies) {
		DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(em, owlClass.getIRI().toString());
		if (dfv != null && dfv.getDefaultAnnotateValuesPaths().size() > 0) {
			return dfv.getDefaultAnnotateValuesPaths();
		}

        for (OWLOntology o : ontologies) {
        	OWLReasoner r = ReasonerResolver.getReasoner(o);
        	for (OWLClass superClass : r.getSuperClasses(owlClass, false).getFlattened()) {
				dfv = DefaultFilterValuesHome.findByIRI(em, superClass.getIRI().toString());
				if (dfv != null && dfv.getDefaultAnnotateValuesPaths().size() > 0) {
					return dfv.getDefaultAnnotateValuesPaths();
				}
        	}
        }

		return Collections.emptyList();
	}
}
