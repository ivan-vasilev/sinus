package bg.bas.iinf.sinus.wicket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.hibernate.dao.DefaultFilterValuesHome;
import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * spisyk s klasovete, koito mogat da se filtrirat
 * @author hok
 *
 */
public class DefaultFilterClassesLDM extends LoadableDetachableModel<List<OWLClass>> {

	private static final long serialVersionUID = 5535609765381820847L;

	private IModel<Set<OWLOntology>> ontologies;

	public DefaultFilterClassesLDM(IModel<Set<OWLOntology>> ontologies) {
		super();
		this.ontologies = ontologies;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		ontologies.detach();
	}

	@Override
	protected List<OWLClass> load() {
		if (ontologies.getObject().size() == 0) {
			return Collections.<OWLClass>emptyList();
		}

		List<DefaultFilterValues> dfvs = DefaultFilterValuesHome.getDefaultFilterValues(ScopedEntityManagerFactory.getEntityManager(), ontologies.getObject());

		OWLDataFactoryImpl.getInstance();
		Set<OWLClass> owlClasses = new HashSet<OWLClass>();
		OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
		for (DefaultFilterValues ddv : dfvs) {
			owlClasses.add(factory.getOWLClass(IRI.create(ddv.getIri())));
		}

		List<OWLClass> result = new ArrayList<OWLClass>();

		// vzimat se samo tezi klasove, koito nqmat roditeli
		skip:
		for (OWLClass owlClass : owlClasses) {
	        for (OWLOntology o : ontologies.getObject()) {
	        	OWLReasoner r = ReasonerResolver.getReasoner(o);
	        	for (OWLClass superClass : r.getSuperClasses(owlClass, false).getFlattened()) {
					if (owlClasses.contains(superClass)) {
						continue skip;
					}
	        	}
	        }

			result.add(owlClass);
		}

		return result;
	}
}
