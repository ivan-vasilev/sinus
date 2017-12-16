package bg.bas.iinf.sinus.wicket.model.owl;

import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.dao.OntologiesHome;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * LDM za edinichna OWLOntology
 *
 * @author hok
 *
 */
public class OWLOntologyLDM extends LoadableDetachableModel<OWLOntology> {

	private static final long serialVersionUID = 5013539617348090497L;

	private String id;

	public OWLOntologyLDM(OWLOntology object) {
		super(object);
		this.id = object.getOntologyID().toString();
	}

	public OWLOntologyLDM(String id) {
		super();
		this.id = id;
	}

	public OWLOntologyLDM() {
		super();
	}

	@Override
	protected OWLOntology load() {
		if (id == null) {
			return null;
		}

		return OntologiesHome.getOntolgoy(ScopedEntityManagerFactory.getEntityManager(), id);
	}

	@Override
    public void setObject(final OWLOntology object) {
		super.setObject(object);
		if (object == null) {
			id = null;
		} else {
			id = object.getOntologyID().toString();
		}
	}

	public String getUri() {
    	return id;
    }
}
