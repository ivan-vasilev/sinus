package bg.bas.iinf.sinus.wicket.owl.filter.searchresults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.SavedSearchesHome;
import bg.bas.iinf.sinus.hibernate.dao.SelectedResultsHome;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchDisplayValues;
import bg.bas.iinf.sinus.hibernate.entity.SearchResults;
import bg.bas.iinf.sinus.hibernate.entity.SelectedResults;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.IModalWindowContainer;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.model.SavedSearchLDM;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.owl.filter.FilterPage;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView.OWLNamedObjectResult;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SelectedResultsPanel.SelectedResultsSubmittedEP;
import bg.bas.iinf.sinus.wicket.page.BasePage;
import css.CSS;

/**
 * Stranica s rezultati ot tyrsene (moje da se izpolzva za tekushto i za stari tyrseniq)
 * @author hok
 *
 */
public class SearchResultsPage extends BasePage {

	private static final long serialVersionUID = -2890494303607721815L;

	public static final String ID = "id";

	private SearchDataProvider dataProvider;
	private Label nrl;

    private IModel<Set<OWLOntology>> ontologies;
    private Set<String> selectedObjects;

    public SearchResultsPage(PageParameters parameters) {
		super(parameters);
		setDefaultModel(new SavedSearchLDM(parameters.get(ID).toOptionalInteger()));
	}

	public SearchResultsPage(IModel<SavedSearches> model) {
		super(model);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		ontologies.detach();
	}

	@SuppressWarnings("unchecked")
    @Override
	protected void onInitialize() {
		super.onInitialize();

		selectedObjects = new HashSet<String>();

		ontologies = new OWLOntologiesLDM(new HashSet<OWLOntology>(new AllOntologiesLDM(true).getObject()));

		// rezultati
		// forma za checkgroup-ata
		final Form<Void> checkForm = new Form<Void>("check_form") {

            private static final long serialVersionUID = -2020722279552107114L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				nrl.configure();
				setVisible(!nrl.isVisible());
			}

		    @Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof ResultSelectedEP) {
					ResultSelectedEP payload = (ResultSelectedEP) event.getPayload();
					payload.getTarget().add(this);
				}
			}
		};
		checkForm.setOutputMarkupId(true);
		checkForm.setOutputMarkupPlaceholderTag(true);
		add(checkForm);

		SavedSearches search = (SavedSearches) getDefaultModelObject();
		if (search.getSavedSearches() != null) {
			checkForm.add(new SearchWithContextFragment("search_data", "search_with_context_fragment", this, (IModel<SavedSearches>) getDefaultModel()));
		} else {
			checkForm.add(new MultiLineLabel("search_data", new PropertyModel<String>(getDefaultModel(), "humanReadable")).setEscapeModelStrings(false));
		}

		// izbirat se vsichki
		checkForm.add(new AjaxCheckBox("groupselector", new Model<Boolean>(false)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (getConvertedInput()) {
					selectedObjects.addAll(dataProvider.getIndividualIRIs());
				} else {
					selectedObjects.clear();
				}

				SavedSearches search = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				search.setAllSelected(getConvertedInput());

				send(getPage(), Broadcast.BREADTH, new GroupSelectedEP(target));
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				SavedSearches ss = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				setVisible(ss.getSavedSearchesId() == null && UserSession.get().getUserId() != null);
			}
		});

		dataProvider = new SearchDataProvider((IModel<SavedSearches>) getDefaultModel(), ontologies);

		// generirat se zaglaviqta na otdelnite koloni (na baza izbrani property-ta za pokazvane)
		RepeatingView rv = new RepeatingView("titles");
		checkForm.add(rv);

		for (SearchDisplayValues sdv : search.getSearchDisplayValueses()) {
			String[] s = sdv.getUriPath().split(",");
			String title = null;
			if (s.length == 1) {
				title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
			} else {
				title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 2]).toURI().toString(), ontologies.getObject()), ontologies) + " -> " + OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
			}
			rv.add(new Label(rv.newChildId(), title));
		}

		// spisyk s rezultati
		DataView<OWLNamedObjectResult<String>> dataView = new SearchResultsDataView<String>("results", search.getObjectUri(), dataProvider, 10) {

            private static final long serialVersionUID = -4744872461512107366L;

			@Override
            protected void populateItem(final Item<OWLNamedObjectResult<String>> bindingItem) {
				super.populateItem(bindingItem);

				IModel<Boolean> model = new LoadableDetachableModel<Boolean>() {

                    private static final long serialVersionUID = -5365429009659575302L;

					@Override
                    protected Boolean load() {
                        return selectedObjects.contains(bindingItem.getModelObject().getIri());
                    }

				};

				bindingItem.add(new AjaxCheckBox("check", model) {

                    private static final long serialVersionUID = 1803734419181574578L;

                    @Override
					public void onEvent(IEvent<?> event) {
						if (event.getPayload() instanceof GroupSelectedEP) {
							GroupSelectedEP payload = (GroupSelectedEP) event.getPayload();
							payload.getTarget().add(this);
						}
					}

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
						if (getConvertedInput()) {
							selectedObjects.add(bindingItem.getModelObject().getIri());
						} else {
							selectedObjects.remove(bindingItem.getModelObject().getIri());
						}
						send(getPage(), Broadcast.BREADTH, new GroupSelectedEP(target));
                    }

        			@Override
        			protected void onConfigure() {
        				super.onConfigure();
        				SavedSearches ss = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
        				setVisible(ss.getSavedSearchesId() == null && UserSession.get().getUserId() != null);
        			}
				});
			}
		};
		checkForm.add(dataView);

		checkForm.add(new AjaxPagingNavigator("paging", dataView));

		nrl = new Label("no_results", new ResourceModel("no_results")) {

            private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				dataProvider.detach();
				setVisible(dataProvider.size() == 0);
			}
		};
		nrl.setOutputMarkupId(true);
		nrl.setOutputMarkupPlaceholderTag(true);
		add(nrl);

		checkForm.add(new IndicatingAjaxButton("new_search") {

            private static final long serialVersionUID = -1504424634823375744L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SavedSearches search = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				if (search.getSavedSearchesId() == null && selectedObjects.size() > 0) {
					for (String s : selectedObjects) {
						search.getSearchResultses().add(new SearchResults(search, s));
					}

					saveSearch();
				}

				target.appendJavaScript("window.parent.location.hash = 'search_" + search.getSavedSearchesId() + "'");

				target.appendJavaScript("window.location = '" + urlFor(FilterPage.class, null) + "'");
			}

			@Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(UserSession.get().getUserId() != null);
			}
		});

		// dobavqne kym izbranite v koshnicata
		checkForm.add(new AjaxButton("add_to_selected") {

			private static final long serialVersionUID = -1504424634823375744L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (selectedObjects.size() == 0) {
					send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new StringResourceModel("choose_results", this, null), NotificationType.ERROR));
					return;
				}

				SavedSearches search = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				search.setIsSelected(true);

				if (search.getSavedSearchesId() == null) {
					for (String s : selectedObjects) {
						search.getSearchResultses().add(new SearchResults(search, s));
					}

					saveSearch();
					search = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				}

				SelectedResultsFilter filter = new SelectedResultsFilter();
				filter.setUserId(UserSession.get().getUserId());
				filter.setClassIRI(search.getObjectUri());
        		if (!StringUtils.isEmpty(UserSession.get().getSearchSessionId())) {
        			filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, UserSession.get().getSearchSessionId()));
        		}
				List<SelectedResults> results = SelectedResultsHome.getSelectedResults(ScopedEntityManagerFactory.getEntityManager(), filter, null);

				Set<String> objectsToAdd = new HashSet<String>();

				skip:
				for (String s : selectedObjects) {
					for (SelectedResults result : results) {
						if (s.equals(result.getObjectIri())) {
							continue skip;
						}
					}

					objectsToAdd.add(s);
				}

				if (objectsToAdd.size() > 0) {
					List<SelectedResults> newResults = new ArrayList<SelectedResults>();
					for (String s : objectsToAdd) {
						newResults.add(new SelectedResults(search, UserSession.get().getUser(), search.getObjectUri(), s));
					}

					Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), newResults);
				}

				PageParameters pp = new PageParameters();
				pp.set(ID, search.getSavedSearchesId());
				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
				send(getPage(), Broadcast.BREADTH, new ResultSelectedEP(target));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				SavedSearches ss = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				setVisible(UserSession.get().getUserId() != null && ss.getSavedSearchesId() == null && UserSession.get().getUserId() != null);
			}
		});

		// pokazvane na izbranite
		checkForm.add(new IndicatingAjaxLink<Void>("selected_results") {

            private static final long serialVersionUID = 7122622004349298642L;

            private transient Integer size;
            @Override
            protected void onDetach() {
            	super.onDetach();
            	size = null;
            }

            @Override
            protected void onInitialize() {
            	super.onInitialize();
            	setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
            }

            @Override
			public void onClick(AjaxRequestTarget target) {
				ModalWindow modal = findParent(IModalWindowContainer.class).getModalWindow();
				modal.setTitle(new StringResourceModel("selected_results", this, null));
				SavedSearches s = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
				SelectedResultsPanel content = new SelectedResultsPanel(modal.getContentId(), s.getObjectUri(), ontologies);
				content.add(new AttributeModifier("style", new LoadableDetachableModel<String>() {

					private static final long serialVersionUID = 3965123518668161711L;

					@Override
					protected String load() {
						return "width:1000px; height:500px;";
					}
				}));
				modal.setContent(content);
				modal.show(target);
			}

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof ResultSelectedEP) {
					((ResultSelectedEP) event.getPayload()).getTarget().add(this);
				} else if (event.getPayload() instanceof SelectedResultsSubmittedEP) {
					SelectedResultsSubmittedEP payload = (SelectedResultsSubmittedEP) event.getPayload();
					payload.getTarget().add(this);
					findParent(IModalWindowContainer.class).getModalWindow().close(payload.getTarget());
					payload.getTarget().appendJavaScript("window.parent.location.hash = 'selected_results_" + UserSession.get().getSearchSessionId() + "'");
				}
			}

            @Override
            protected void onConfigure() {
            	super.onConfigure();
            	setVisible(getSize() > 0);
            	if (isVisible()) {
            		setBody(new StringResourceModel("selected_results_count", this, null, getSize()));
            	}
            }

            private Integer getSize() {
            	if (size == null) {
            		if (UserSession.get().getUserId() == null) {
            			size = 0;
            		} else {
                		SelectedResultsFilter filter = new SelectedResultsFilter();
                		filter.setUserId(UserSession.get().getUserId());
                		if (!StringUtils.isEmpty(UserSession.get().getSearchSessionId())) {
                			filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, UserSession.get().getSearchSessionId()));
                		}
        				SavedSearches s = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
                		filter.setClassIRI(s.getObjectUri());
                		size = SelectedResultsHome.getSelectedResultsCount(ScopedEntityManagerFactory.getEntityManager(), filter).intValue();
            		}
            	}

            	return size;
            }
		});
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("search_results", this, null);
	}

	/**
	 * zapomnqne na tova tyrsene
	 */
	private void saveSearch() {
		SavedSearches search = (SavedSearches) SearchResultsPage.this.getDefaultModelObject();
		if (StringUtils.isEmpty(search.getTag())) {
			search.setTag(UserSession.get().getSearchSessionId());
		}
		SavedSearchesHome.persistOrMergeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), search);
		SearchResultsPage.this.setDefaultModel(new SavedSearchLDM(search));
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("search_results", this, null);
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderCSSReference(new PackageResourceReference(CSS.class, "list.css"));
		response.renderCSSReference(new PackageResourceReference(CSS.class, "navigation.css"));
	}

	private static class GroupSelectedEP {

		private AjaxRequestTarget target;

		public GroupSelectedEP(AjaxRequestTarget target) {
			super();
			this.target = target;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}
	}

	/**
	 * zapomneno tyrsene + contexta mu
	 * @author hok
	 *
	 */
	private static class SearchWithContextFragment extends Fragment {

        private static final long serialVersionUID = -3487870384026006950L;

		public SearchWithContextFragment(String id, String markupId, MarkupContainer markupProvider, IModel<SavedSearches> model) {
	        super(id, markupId, markupProvider, model);
	        model.getObject().setSavedSearches(Home.findById(ScopedEntityManagerFactory.getEntityManager(), model.getObject().getSavedSearches().getSavedSearchesId(), SavedSearches.class));
        }

		@Override
		protected void onInitialize() {
			super.onInitialize();

			add(new MultiLineLabel("humanReadable", new PropertyModel<String>(getDefaultModel(), "humanReadable")).setEscapeModelStrings(false));

			add(new Label("context_uris", new LoadableDetachableModel<String>() {

                private static final long serialVersionUID = 6024596146225012475L;

				@Override
				protected String load() {
					SavedSearches s = (SavedSearches) getDefaultModelObject();
					List<String> uris = new ArrayList<String>();
					for (SearchResults r : s.getSavedSearches().getSearchResultses()) {
						uris.add(r.getResult());
					}

					return StringUtils.join(uris, ", ");
				}
			}));

			add(new MultiLineLabel("contextHumanReadable", new PropertyModel<String>(getDefaultModel(), "savedSearches.humanReadable")).setEscapeModelStrings(false));
		}
	}

	private static class ResultSelectedEP {
		private AjaxRequestTarget target;

		public ResultSelectedEP(AjaxRequestTarget target) {
			super();
			this.target = target;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}
	}
}