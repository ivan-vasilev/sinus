package bg.bas.iinf.sinus.wicket.owl.filter.searchresults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.dao.DefaultDisplayValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.SelectedResultsHome;
import bg.bas.iinf.sinus.hibernate.entity.DefaultDisplayValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.SelectedResults;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.common.RefreshListEP;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;

public class SelectedResultsPanel extends GenericPanel<SelectedResultsFilter> {

    private static final long serialVersionUID = 2281403778788541239L;

    private IModel<Set<OWLOntology>> ontologies;

    public SelectedResultsPanel(String id, String classIRI, IModel<Set<OWLOntology>> ontologies) {
	    super(id);
	    this.ontologies = ontologies;

	    SelectedResultsFilter filter = new SelectedResultsFilter();
	    filter.setUserId(UserSession.get().getUserId());
	    filter.setClassIRI(classIRI);
		if (!StringUtils.isEmpty(UserSession.get().getSearchSessionId())) {
			filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, UserSession.get().getSearchSessionId()));
		}
	    setModel(new Model<SelectedResultsFilter>(filter));
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final SelectedResultsDataProvider dataProvider = new SelectedResultsDataProvider(ontologies);

		final WebMarkupContainer container = new WebMarkupContainer("container") {

            private static final long serialVersionUID = 5644191572190598702L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(dataProvider.size() > 0);
			}

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RefreshListEP) {
					((RefreshListEP) event.getPayload()).getTarget().add(this);
				}
			}
		};
		add(container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		RepeatingView rv = new RepeatingView("titles");
		container.add(rv);

		OWLClass owlClass = OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(getModelObject().getClassIRI()));
		List<DefaultDisplayValuesPath> paths = DefaultDisplayValuesHome.getDefaultDisplayValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClass, ontologies.getObject());
		for (DefaultDisplayValuesPath path : paths) {
			String[] s = path.getPath().split(",");
			String title = null;
			if (s.length == 1) {
				title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
			} else {
				title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 2]).toURI().toString(), ontologies.getObject()), ontologies) + " -> " + OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
			}
			rv.add(new Label(rv.newChildId(), title));
		}

		SearchResultsDataView<Integer> dataView = new SearchResultsDataView<Integer>("results", getModelObject().getClassIRI(), dataProvider, getResultsPerPage()) {

            private static final long serialVersionUID = -9126273816993062959L;

			@Override
			protected void populateItem(Item<OWLNamedObjectResult<Integer>> bindingItem) {
				super.populateItem(bindingItem);

				PageParameters pp = new PageParameters();
				pp.set(SearchResultsPage.ID, dataProvider.getContexId(bindingItem.getModelObject().getIri()));
				bindingItem.add(new BookmarkablePageLink<SearchResultsPage>("view_context", SearchResultsPage.class, pp));

				bindingItem.add(new IndicatingAjaxLink<OWLNamedObjectResult<Integer>>("delete", bindingItem.getModel()) {

					private static final long serialVersionUID = 4848768937043310095L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						SelectedResults s = Home.findById(ScopedEntityManagerFactory.getEntityManager(), getModelObject().getIri(), SelectedResults.class);
						Home.removeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), s);
						send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
						send(getPage(), Broadcast.BREADTH, new RefreshListEP(target));
					}
				});
			}
		};
		container.add(dataView);
		container.add(new AjaxLink<Void>("submit_results") {

            private static final long serialVersionUID = -7607644503163207992L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				EntityManager em = ScopedEntityManagerFactory.getEntityManager();
				Home.persistInOneTransaction(em, SelectedResultsHome.getSelectedResults(em, SelectedResultsPanel.this.getModelObject(), null));
				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
				send(getPage(), Broadcast.BREADTH, new SelectedResultsSubmittedEP(target));
			}
		});

		AjaxPagingNavigator paging = new AjaxPagingNavigator("paging", dataView);
		container.add(paging);

		add(new Label("no_results", new ResourceModel("no_results")) {

            private static final long serialVersionUID = 6477316809452402514L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RefreshListEP) {
					((RefreshListEP) event.getPayload()).getTarget().add(this);
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				container.configure();
				setVisible(!container.isVisible());
			}
		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
	}

	protected Integer getResultsPerPage() {
		return 5;
	}

	private class SelectedResultsDataProvider extends LiftingDataProvider<Integer> {

        private static final long serialVersionUID = -6564666897009173552L;

        private transient List<SelectedResults> selectedResults;

		public SelectedResultsDataProvider(IModel<Set<OWLOntology>> ontologies) {
	        super(ontologies);
        }

		@Override
		public void detach() {
			super.detach();
			selectedResults = null;
		}

		@Override
		protected String getRootClassIRI() {
			return getModelObject().getClassIRI();
		}

		@Override
		public List<String> getIndividualIRIs() {
			List<String> result = new ArrayList<String>();

			selectedResults = SelectedResultsHome.getSelectedResults(ScopedEntityManagerFactory.getEntityManager(), getModelObject(), null);
			for (SelectedResults s : selectedResults) {
				result.add(s.getObjectIri());
			}

			return result;
		}

		@Override
		protected List<List<String>> getDisplayPaths() {
			List<List<String>> result = new ArrayList<List<String>>();
			OWLClass owlClass = OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(getModelObject().getClassIRI()));
			List<DefaultDisplayValuesPath> paths = DefaultDisplayValuesHome.getDefaultDisplayValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClass, ontologies.getObject());
			for (DefaultDisplayValuesPath path : paths) {
				result.add(new ArrayList<String>(Arrays.asList(path.getPath().split(","))));
			}

			return result;
		}

		@Override
        protected Integer getTForUri(String uri) {
			for (SelectedResults s : selectedResults) {
				if (s.getObjectIri().equals(uri)) {
					return s.getSelectedResultsId();
				}
			}

			return null;
		};

		public Integer getContexId(Integer selectedResultId) {
			for (SelectedResults s : selectedResults) {
				if (s.getSelectedResultsId().equals(selectedResultId)) {
					return s.getSavedSearches().getSavedSearchesId();
				}
			}

			return null;
		};
	}

	public static class SelectedResultsSubmittedEP {
		private AjaxRequestTarget target;

		public SelectedResultsSubmittedEP(AjaxRequestTarget target) {
	        super();
	        this.target = target;
        }
		public AjaxRequestTarget getTarget() {
        	return target;
        }
	}
}