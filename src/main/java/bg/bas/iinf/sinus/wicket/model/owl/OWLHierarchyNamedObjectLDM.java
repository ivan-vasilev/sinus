package bg.bas.iinf.sinus.wicket.model.owl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLNamedObject;

/**
 * Ierarhichen OWLNamedObjectLDM - izpolzva se v dyrveta, etc
 * pozvolqva da se izgradi dyrvo ot OWLNamedObject-i
 * @author hok
 *
 * @param <T>
 */
public class OWLHierarchyNamedObjectLDM<T extends OWLNamedObject> extends OWLNamedObjectLDM<T> {

    private static final long serialVersionUID = 8956303760525108532L;

    private IModel<? extends OWLNamedObject> parent;

	public OWLHierarchyNamedObjectLDM(T object, IModel<? extends OWLNamedObject> parent) {
	    super(object);
	    this.parent = parent;
    }

	public IModel<? extends OWLNamedObject> getParent() {
    	return parent;
    }

	public void setParent(IModel<? extends OWLNamedObject> parent) {
    	this.parent = parent;
    }

	@SuppressWarnings("unchecked")
    @Override
	public String toString() {
		List<String> paths = new ArrayList<String>();
		paths.add(getObject().getIRI().toString());
		IModel<? extends OWLNamedObject> p = parent;
		while (p != null) {
			paths.add(p.getObject().toString());
			if (p instanceof OWLHierarchyNamedObjectLDM) {
				p = ((OWLHierarchyNamedObjectLDM<T>) p).getParent();
			} else {
				p = null;
			}
		}

		Collections.reverse(paths);
		return StringUtils.join(paths, ";");
	}
}
