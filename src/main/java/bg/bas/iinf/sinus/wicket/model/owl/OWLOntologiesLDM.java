package bg.bas.iinf.sinus.wicket.model.owl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.cache.CacheWrapper;
import bg.bas.iinf.sinus.hibernate.dao.OntologiesHome;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * LDM za mnogo ontologii
 * @author hok
 *
 */
public class OWLOntologiesLDM extends LoadableDetachableModel<Set<OWLOntology>> {

    private static final long serialVersionUID = -2005826636168615671L;

    private Set<String> ids;

	public OWLOntologiesLDM(Set<OWLOntology> object) {
	    super();
	    setObject(object);
    }

	public OWLOntologiesLDM() {
		super();
	}

	@Override
	public void setObject(Set<OWLOntology> object) {
		super.setObject(object);

		if (ids == null) {
			ids = new HashSet<String>();
		} else {
			ids.clear();
		}

	    for (OWLOntology ont : object) {
	    	ids.add(ont.getOntologyID().toString());
	    }
	}

	@Override
    protected Set<OWLOntology> load() {
		Set<OWLOntology> result = new HashSet<OWLOntology>();

		for (String id : ids) {
			result.add(OntologiesHome.getOntolgoy(ScopedEntityManagerFactory.getEntityManager(), id));
		}

		return result;
    }

	public static OWLNamedObject getOWLNamedObject(String uri, Set<OWLOntology> ontologies) {
		OWLNamedObject o = null;
		endAll:
		for (OWLOntology ont : ontologies) {
			for (OWLClass c : ont.getClassesInSignature()) {
				if (c.getIRI().toURI().toString().equals(uri)) {
					o = c;
					break endAll;
				}
			}

			for (OWLObjectProperty p : ont.getObjectPropertiesInSignature()) {
				if (p.getIRI().toURI().toString().equals(uri)) {
					o = p;
					break endAll;
				}
			}

			for (OWLDataProperty p : ont.getDataPropertiesInSignature()) {
				if (p.getIRI().toURI().toString().equals(uri)) {
					o = p;
					break endAll;
				}
			}

			for (OWLNamedIndividual ind : ont.getIndividualsInSignature()) {
				if (ind.getIRI().toURI().toString().equals(uri)) {
					o = ind;
					break endAll;
				}
			}
		}

		if (o != null) {
			CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
			try {
	            cache.put(new URI(uri), o);
            } catch (URISyntaxException e) {
	            e.printStackTrace();
            }
		}

		return o;
	}

	public Set<String> getIds() {
    	return ids;
    }

	public void setIds(Set<String> idis) {
    	this.ids = idis;
    }
}
