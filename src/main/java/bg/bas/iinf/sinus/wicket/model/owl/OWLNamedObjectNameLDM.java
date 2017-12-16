package bg.bas.iinf.sinus.wicket.model.owl;

import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.string.Strings;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.wicket.owl.LabelExtractor;

/**
 * ime na obekt
 *
 * @author hok
 *
 */
public class OWLNamedObjectNameLDM<T extends OWLNamedObject> extends LoadableDetachableModel<String> {

	private static final long serialVersionUID = -1524080832207626512L;

	private IModel<T> model;
	private IModel<Set<OWLOntology>> ontologies;

	public OWLNamedObjectNameLDM(IModel<T> namedObject, IModel<Set<OWLOntology>> ontologies) {
		super();
		this.model = namedObject;
		this.ontologies = ontologies;
	}

	@Override
	protected String load() {
		return load(model.getObject(), ontologies);
	}

	public static <T extends OWLNamedObject> String load(T namedObject, IModel<Set<OWLOntology>> ontologies) {
		if (namedObject == null) {
			return "";
		}

		LabelExtractor le = new LabelExtractor();

		if (namedObject instanceof OWLEntity) {
			OWLEntity entity = (OWLEntity) namedObject;

			for (OWLOntology ont : ontologies.getObject()) {
				for (OWLAnnotation anno : entity.getAnnotations(ont)) {
					anno.accept(le);
				}
			}

			if (!Strings.isEmpty(le.getResult())) {
				return le.getResult();
			}
		}

		return namedObject.getIRI().toQuotedString();
	}
}
