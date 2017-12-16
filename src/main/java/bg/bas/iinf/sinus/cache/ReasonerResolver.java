package bg.bas.iinf.sinus.cache;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


/**
 * vryshta reasoner za ontologiq
 *
 * @author hok
 *
 */
public class ReasonerResolver {

	private ReasonerResolver() {
	}

	/**
	 * izpolzva cache-a, ako reasoner-a go nqma tam se opitva da go syzdade
	 *
	 * @param ontologyURI - URI na ontologiqta, za koqto e reasoner-a
	 * @return
	 */
	public static final OWLReasoner getReasoner(OWLOntology ont) {
		OWLReasoner result = CacheFactory.getReasonerCache().get(ont.getOntologyID().toString() + "reasoner");
		if (result == null) {
			result = (new StructuralReasonerFactory()).createReasoner(ont);

			CacheFactory.getReasonerCache().put(ont.getOntologyID().toString() + "reasoner", result);
		}

		return result;
	}
}
