package bg.bas.iinf.sinus.wicket.owl;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel.RootNodeSelectedEP;

/**
 * pokazva menu ot tip dijit.Menu s podklasovete na daden klas
 * @author hok
 *
 */
public class OWLClassesMenuComponent extends OWLMenu {

    private static final long serialVersionUID = 8351268630482975997L;

	public OWLClassesMenuComponent(String id, IModel<? extends OWLNamedObject> model, IModel<Set<OWLOntology>> ontologies) {
	    super(id, model, ontologies);
	    this.ontologies = ontologies;
    }

	@Override
    protected void onObjectSelected(AjaxRequestTarget target, IModel<? extends OWLNamedObject> model) {
		OWLNamedObjectLDM<OWLClass> m = new OWLNamedObjectLDM<OWLClass>((OWLClass) model.getObject());
        send(getPage(), Broadcast.BREADTH, new RootNodeSelectedEP(target, m));
	}

	@Override
    protected Set<OWLClass> getRootObjects() {
		Set<OWLClass> result = new HashSet<OWLClass>();
		OWLClass parentClass = (OWLClass) getDefaultModelObject();
		for (OWLOntology ont : ontologies.getObject()) {
			OWLReasoner reasoner = ReasonerResolver.getReasoner(ont);
			result.addAll(reasoner.getSubClasses(parentClass, true).getFlattened());
		}

		return result;
    }

	@Override
    protected Set<OWLClass> getObjectChildren(OWLNamedObject parent) {
		Set<OWLClass> children = new HashSet<OWLClass>();
		OWLClass parentClass = (OWLClass) parent;
		for (OWLOntology ont : ontologies.getObject()) {
			OWLReasoner reasoner = ReasonerResolver.getReasoner(ont);
			children.addAll(reasoner.getSubClasses(parentClass, true).getFlattened());
		}

		return children;
    };
}
