package bg.bas.iinf.sinus.wicket.owl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.owl.SelectOntologiesPanel.OntologiesSelectedEP;
import css.CSS;

/**
 * spisyk s "Root" obektite na ontologiq - moje da sa takiva, koito nqmat roditeli ili takiva, koito stavat za filtrirane
 * @author hok
 *
 */
public class RootObjectsPanel extends GenericPanel<List<OWLClass>> {

	private static final long serialVersionUID = -1853456979661047247L;

	private IModel<Set<OWLOntology>> ontologies;

	public RootObjectsPanel(String id, IModel<List<OWLClass>> model, IModel<Set<OWLOntology>> ontologies) {
		super(id, model);
		this.ontologies = ontologies;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		ontologies.detach();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);

		add(new RootClassesRefreshingView("data_view", getModel()));
	}

	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof OntologiesSelectedEP) {
			OntologiesSelectedEP payload = (OntologiesSelectedEP) event.getPayload();
			payload.getTarget().add(this);
			DojoUtils.refreshWidgets(payload.getTarget(), this);
		}
	}

	private class RootClassesRefreshingView extends RefreshingView<OWLClass> {

		private static final long serialVersionUID = -6732630245495899808L;

		protected RootClassesRefreshingView(String id, IModel<List<OWLClass>> model) {
			super(id, model);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void populateItem(Item<OWLClass> item) {
			Set<OWLClass> children = new HashSet<OWLClass>();
			for (OWLOntology ont : ontologies.getObject()) {
				OWLReasoner reasoner = ReasonerResolver.getReasoner(ont);
				children.addAll(reasoner.getSubClasses(item.getModelObject(), true).getFlattened());
			}

			if (children.size() == 0 || (children.size() == 1 && children.iterator().next().getIRI().equals(OWLRDFVocabulary.OWL_NOTHING.getIRI()))) {
				item.add(new Fragment("class", "button_fragment", this, item.getModel()) {

					private static final long serialVersionUID = -7655123236584563611L;

					@Override
					protected void onInitialize() {
						super.onInitialize();

						AjaxLink<OWLClass> link = new AjaxLink<OWLClass>("link", (IModel<OWLClass>) getDefaultModel()) {

							private static final long serialVersionUID = -522000391869563084L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								send(getPage(), Broadcast.BREADTH, new RootNodeSelectedEP(target, getModel()));
							}
						};
						link.setBody(new OWLNamedObjectNameLDM<OWLClass>(link.getModel(), ontologies));
						add(link);
					}
				});
			} else {
				item.add(new OWLClassComboButton("class", item.getModel(), ontologies));
			}
		}

		@SuppressWarnings("unchecked")
        @Override
        protected Iterator<IModel<OWLClass>> getItemModels() {
			List<IModel<OWLClass>> result = new ArrayList<IModel<OWLClass>>();

			for (OWLClass c : (List<OWLClass>) getDefaultModelObject()) {
				result.add(new OWLNamedObjectLDM<OWLClass>(c));
			}

			return result.iterator();
        }
	};

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderCSSReference(new PackageResourceReference(CSS.class, "list.css"));
	}

	public static class RootNodeSelectedEP {

		private AjaxRequestTarget target;
		private IModel<OWLClass> selectedClass;

		public RootNodeSelectedEP(AjaxRequestTarget target, IModel<OWLClass> selectedClass) {
			super();
			this.target = target;
			this.selectedClass = selectedClass;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public void setTarget(AjaxRequestTarget target) {
			this.target = target;
		}

		public IModel<OWLClass> getSelectedClass() {
			return selectedClass;
		}

		public void setSelectedClass(IModel<OWLClass> selectedClass) {
			this.selectedClass = selectedClass;
		}
	}
}
