package bg.bas.iinf.sinus.wicket.model.owl;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.dao.OntologiesHome;
import bg.bas.iinf.sinus.hibernate.entity.Ontologies;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * LDM sys vsichki dobaveni OWLOntology obekti
 * @author hok
 *
 */
public class AllOntologiesLDM extends LoadableDetachableModel<List<OWLOntology>> {

    private static final long serialVersionUID = 2984589910083268880L;

    private Boolean isConfigured;

    public AllOntologiesLDM(Boolean isConfigured) {
	    super();
	    this.isConfigured = isConfigured;
    }

	@Override
    protected List<OWLOntology> load() {
        List<OWLOntology> result = new ArrayList<OWLOntology>();
        for (Ontologies o : OntologiesHome.getOntologies(ScopedEntityManagerFactory.getEntityManager(), isConfigured)) {
			result.add(OntologiesHome.getOntolgoy(ScopedEntityManagerFactory.getEntityManager(), o.getId()));
		}

        return result;
	}
}