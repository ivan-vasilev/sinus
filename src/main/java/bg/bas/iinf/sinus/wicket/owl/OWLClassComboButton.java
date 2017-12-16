package bg.bas.iinf.sinus.wicket.owl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel.RootNodeSelectedEP;
import css.CSS;

/**
 * Combo buton s klas i negovite podklasove
 * @author hok
 *
 */
public class OWLClassComboButton extends Panel {

	private static final Log log = LogFactory.getLog(OWLClassComboButton.class);

    private static final long serialVersionUID = 7251832587752120083L;

	private IModel<Set<OWLOntology>> ontologies;
	private AbstractDefaultAjaxBehavior classSelector;

    public OWLClassComboButton(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
	    super(id, model);
	    this.ontologies = ontologies;
    }

	@SuppressWarnings("unchecked")
    @Override
	protected void onInitialize() {
		super.onInitialize();

		setRenderBodyOnly(false);

		add(AttributeModifier.replace("dojoType", "dijit.form.ComboButton"));
		add(AttributeModifier.replace("class", "dropDownCombo"));

        classSelector = new AbstractDefaultAjaxBehavior() {

			private static final long serialVersionUID = 5734106819456340781L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters parameters = RequestCycle.get().getRequest().getQueryParameters();
				if (parameters.getParameterNames().contains("uri")) {
					try {
	                    URI uri = new URI(parameters.getParameterValue("uri").toString());
	                    send(getPage(), Broadcast.BREADTH, new RootNodeSelectedEP(target, new OWLNamedObjectLDM<OWLClass>(uri)));
                    } catch (URISyntaxException e) {
                    	log.error(e);
                    }
				}
			}
		};
		add(classSelector);

        Label changeLocationLabel = new Label("button_event") {

			private static final long serialVersionUID = 7345876365107001750L;

			@Override
        	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {

        		OWLClass parentClass = (OWLClass) OWLClassComboButton.this.getDefaultModelObject();

        		String uri = "";
        		try {
                    uri = URLEncoder.encode(parentClass.getIRI().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e);
                }

                String value = String.format("wicketAjaxGet(\"%s&uri=%s\", function() { }, function() { });", classSelector.getCallbackUrl(), uri);

        		replaceComponentTagBody(markupStream, openTag, value);
        	}

        };
		changeLocationLabel.setEscapeModelStrings(false);
		add(changeLocationLabel);

		OWLClass parentClass = (OWLClass) getDefaultModelObject();
		add(new Label("button_text", OWLNamedObjectNameLDM.load(parentClass, ontologies)));

		add(new OWLClassesMenuComponent("classes_menu", (IModel<OWLClass>) getDefaultModel(), ontologies));
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScript("dojo.require('dijit.form.ComboButton');", "dijit.form.ComboButton");
		response.renderCSSReference(new PackageResourceReference(CSS.class, "dojo.css"));
	}
}
