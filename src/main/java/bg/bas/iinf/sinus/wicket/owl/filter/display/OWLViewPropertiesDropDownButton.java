package bg.bas.iinf.sinus.wicket.owl.filter.display;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import css.CSS;

public class OWLViewPropertiesDropDownButton extends Panel {

	private static final long serialVersionUID = 7251832587752120083L;

	private IModel<Set<OWLOntology>> ontologies;

	public OWLViewPropertiesDropDownButton(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
		super(id, model);
		this.ontologies = ontologies;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize() {
		super.onInitialize();

		setRenderBodyOnly(false);
		setOutputMarkupId(true);

		add(AttributeModifier.replace("dojoType", "dijit.form.DropDownButton"));
		add(AttributeModifier.replace("class", "dropDownCombo"));

		IModel<Set<OWLOntology>> allOntologies = new LoadableDetachableModel<Set<OWLOntology>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected Set<OWLOntology> load() {
				return new HashSet<OWLOntology>(new AllOntologiesLDM(null).getObject());
			}
		};
		add(new Label("name", new OWLNamedObjectNameLDM<OWLClass>((IModel<OWLClass>) getDefaultModel(), allOntologies)));

		addMenu("classes_menu");
	}

	@SuppressWarnings("unchecked")
    protected void addMenu(String id) {
		add(new OWLDisplayMenu(id, (IModel<OWLClass>) getDefaultModel(), ontologies));
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScript("dojo.require('dijit.form.Button');", "dijit.form.Button");
		response.renderCSSReference(new PackageResourceReference(CSS.class, "dojo.css"));
	}
}
