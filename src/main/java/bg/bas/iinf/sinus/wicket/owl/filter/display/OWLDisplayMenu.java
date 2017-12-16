package bg.bas.iinf.sinus.wicket.owl.filter.display;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.owl.ChildPropertiesVisitor;
import bg.bas.iinf.sinus.wicket.owl.OWLHierarchicalMenu;

public class OWLDisplayMenu extends OWLHierarchicalMenu {

    private static final long serialVersionUID = 3444403414822140556L;

	public OWLDisplayMenu(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
	    super(id, model, ontologies);
    }

	@Override
	protected Collection<? extends OWLNamedObject> getRootObjects() {
		OWLClass owlClass = (OWLClass) getDefaultModelObject();
		Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
		result.addAll(new ChildPropertiesVisitor(owlClass, ontologies.getObject()).getChildProperties());
        for (OWLOntology o : ontologies.getObject()) {
        	OWLReasoner r = ReasonerResolver.getReasoner(o);
        	for (OWLClass superClass : r.getSuperClasses(owlClass, true).getFlattened()) {
        		result.addAll(new ChildPropertiesVisitor(superClass, ontologies.getObject()).getChildProperties());
        	}

        	for (OWLClass superClass : r.getSuperClasses(owlClass, false).getFlattened()) {
        		result.addAll(new ChildPropertiesVisitor(superClass, ontologies.getObject()).getChildProperties());
        	}
        }

		return result;
	}

	@Override
	protected Collection<? extends OWLNamedObject> getObjectChildren(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel) {
		Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
		OWLNamedObject parent = parentModel.getObject();

		if (parent instanceof OWLClass) {
			for (OWLClassExpression subclass : ((OWLClass) parent).getSubClasses(ontologies.getObject())) {
				for (OWLClass owlClass : subclass.getClassesInSignature()) {
					if (hasDataProperties(owlClass)) {
						result.add(owlClass);
					}
				}
			}

			result.addAll(new ChildPropertiesVisitor((OWLClass) parent, ontologies.getObject()).getChildProperties());
		} else if (parent instanceof OWLObjectProperty) {
			OWLObjectProperty objectProperty = (OWLObjectProperty) parent;
			for (OWLClassExpression rangeEntry : objectProperty.getRanges(ontologies.getObject())) {
				result.addAll(rangeEntry.getClassesInSignature());
			}
		}

		return result;
	}

	private boolean hasDataProperties(OWLNamedObject object) {
		if (object instanceof OWLDataProperty) {
			return true;
		} else if (object instanceof OWLObjectProperty) {
			OWLObjectProperty objectProperty = (OWLObjectProperty) object;
			for (OWLClassExpression rangeEntry : objectProperty.getRanges(ontologies.getObject())) {
				for (OWLClass owlClass : rangeEntry.getClassesInSignature()) {
					if (hasDataProperties(owlClass)) {
						return true;
					}
				}
			}
		} else if (object instanceof OWLClass) {
			if (new ChildPropertiesVisitor((OWLClass) object, ontologies.getObject()).getChildProperties().size() > 0) {
				return true;
			}

			for (OWLClassExpression subclass : ((OWLClass) object).getSubClasses(ontologies.getObject())) {
				for (OWLClass owlClass : subclass.getClassesInSignature()) {
					if (hasDataProperties(owlClass)) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
