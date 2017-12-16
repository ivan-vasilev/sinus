package bg.bas.iinf.sinus.wicket.owl;

import java.util.Collection;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;

/**
 * Panel za izbor na ontologii
 * @author hok
 *
 */
public class SelectOntologiesPanel extends GenericPanel<Set<OWLOntology>> {

	private static final long serialVersionUID = 2501957886251838500L;

	public SelectOntologiesPanel(String id, IModel<Set<OWLOntology>> model) {
		super(id, model);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<Void> form = new ErrorHighlightingForm<Void>("form");
		form.setOutputMarkupId(true);
		add(form);

		form.add(new OWLOntologiesMultipleChoice("ontologies", (IModel) SelectOntologiesPanel.this.getModel()));

		form.add(new IndicatingAjaxButton("submit") {

			private static final long serialVersionUID = 868278946883555649L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				send(getPage(), Broadcast.BREADTH, new OntologiesSelectedEP(target, SelectOntologiesPanel.this.getModelObject()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(form);
			}
		});
	}

	public static class OntologiesSelectedEP {
		private AjaxRequestTarget target;
		private Set<OWLOntology> ontologies;

		public OntologiesSelectedEP(AjaxRequestTarget target, Set<OWLOntology> ontologies) {
			super();
			this.target = target;
			this.ontologies = ontologies;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public Set<OWLOntology> getOntologies() {
			return ontologies;
		}
	}

	private static class OWLOntologiesMultipleChoice extends CheckBoxMultipleChoice<OWLOntology> {

		private static final long serialVersionUID = 8079898277275195003L;

		public OWLOntologiesMultipleChoice(String id, IModel<Collection<OWLOntology>> model) {
			super(id, model, new AllOntologiesLDM(true), new IChoiceRenderer<OWLOntology>() {

				private static final long serialVersionUID = 7595691842039927674L;

				@Override
				public Object getDisplayValue(OWLOntology object) {
					return object.getOntologyID().getOntologyIRI().toString();
				}

				@Override
				public String getIdValue(OWLOntology object, int index) {
					return object.getOntologyID().getOntologyIRI().toString();
				}
			});
		}
	}
}
