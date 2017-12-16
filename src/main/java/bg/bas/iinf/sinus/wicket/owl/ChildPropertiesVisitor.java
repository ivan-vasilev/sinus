package bg.bas.iinf.sinus.wicket.owl;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import bg.bas.iinf.sinus.cache.ReasonerResolver;

/**
 * visitor za izvlichane na vsichki property-ta na daden klas
 * @author hok
 *
 */
public class ChildPropertiesVisitor implements OWLPropertyExpressionVisitor {

	private Set<OWLProperty<?, ?>> childProperties;
	private Set<OWLOntology> ontologies;
	private Set<OWLClass> parentClasses;

	public ChildPropertiesVisitor(OWLClass parentClass, Set<OWLOntology> ontologies) {
        this.parentClasses = new HashSet<OWLClass>();
        parentClasses.add(parentClass);

        this.ontologies = ontologies;
        childProperties = new HashSet<OWLProperty<?, ?>>();

        addEquivalentClasses();

		for (OWLOntology ont : ontologies) {
			for (OWLObjectProperty op : ont.getObjectPropertiesInSignature()) {
				op.accept(this);
			}

			for (OWLDataProperty op : ont.getDataPropertiesInSignature()) {
				op.accept(this);
			}
		}
	}

	private void addEquivalentClasses() {
		for (OWLOntology o : ontologies) {
			OWLReasoner r = ReasonerResolver.getReasoner(o);
			for (OWLClass parentClass : parentClasses) {
				parentClasses.addAll(r.getEquivalentClasses(parentClass).getEntities());
			}
		}
	}

	@Override
    public void visit(OWLObjectProperty property) {
		breakAll:
		for (OWLClassExpression classExp : property.getDomains(ontologies)) {
			for (OWLClass c : classExp.getClassesInSignature()) {
				for (OWLClass parentClass : parentClasses) {
					if (c.getIRI().equals(parentClass.getIRI())) {
						childProperties.add(property);
						break breakAll;
					}
				}
			}
		}
    }

	@Override
    public void visit(OWLObjectInverseOf property) {
		// do nothing
    }

	@Override
    public void visit(OWLDataProperty property) {
		breakAll:
		for (OWLClassExpression classExp : property.getDomains(ontologies)) {
			for (OWLClass c : classExp.getClassesInSignature()) {
				for (OWLClass parentClass : parentClasses) {
					if (c.getIRI().equals(parentClass.getIRI())) {
						childProperties.add(property);
						break breakAll;
					}
				}
			}
		}
    }

	public Set<OWLProperty<?, ?>> getChildProperties() {
    	return childProperties;
    }
}
