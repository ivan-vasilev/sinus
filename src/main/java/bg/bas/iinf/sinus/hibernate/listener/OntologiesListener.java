package bg.bas.iinf.sinus.hibernate.listener;

import javax.persistence.PreRemove;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.hibernate.entity.Ontologies;

/**
 * listener za Ontologies
 * @author hok
 *
 */
public class OntologiesListener {

	@PreRemove
	void onPreRemove(Object o) throws IllegalArgumentException {
		if (!(o instanceof Ontologies)) {
			throw new IllegalArgumentException("Argument must be of type Ontologies");
		}

		Ontologies transientInstance = (Ontologies) o;

		CacheFactory.getOntologyCache().remove(transientInstance.getId());
	}
}
