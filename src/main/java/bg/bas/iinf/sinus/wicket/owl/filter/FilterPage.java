package bg.bas.iinf.sinus.wicket.owl.filter;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.cache.CacheWrapper;
import bg.bas.iinf.sinus.hibernate.dao.DefaultDisplayValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.SelectedResultsHome;
import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.DefaultDisplayValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchDisplayValues;
import bg.bas.iinf.sinus.hibernate.entity.SelectedResults;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.repository.QueryUtil;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.ComponentsFragment;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.IModalWindowContainer;
import bg.bas.iinf.sinus.wicket.model.DefaultFilterClassesLDM;
import bg.bas.iinf.sinus.wicket.model.HumanReadableLDM;
import bg.bas.iinf.sinus.wicket.model.SavedSearchLDM;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel.RootNodeSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.SelectOntologiesPanel;
import bg.bas.iinf.sinus.wicket.owl.SelectOntologiesPanel.OntologiesSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.filter.display.OWLClassDisplayPanel;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchDataProvider;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView.OWLNamedObjectResult;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsPage;
import bg.bas.iinf.sinus.wicket.page.BasePage;
import bg.bas.iinf.sinus.wicket.search.SavedSearchesFragment;
import bg.bas.iinf.sinus.wicket.search.SavedSearchesFragment.SearchSelectedEP;
import bg.bas.iinf.sinus.wicket.search.SelectSessionPanel;
import bg.bas.iinf.sinus.wicket.search.SelectSessionPanel.SessionSelectedEP;

/**
 * stranica za filtyr
 * @author hok
 *
 */
//AuthorizeInstantiation({AuthRoles.LIBRARIAN_CONST, AuthRoles.ADMIN_CONST, AuthRoles.STUDENT_CONST}) TODO
public class FilterPage extends BasePage {

	private static final long serialVersionUID = -2890494303607721815L;

	private static final String USER_NAME_PARAM = "user";
	private static final String SESSION_NAME_PARAM = "session";
	private static final String URIS_PARAM = "uris";

	private static final String PROPERTIES_ID = "properties";
	private static final String FORM_ID = "form";
	private static final String PROPERTIES_DISPLAY_ID = "properties_display";
	private static final String SAVED_SEARCHES_ID = "saved_search_id";

	private SavedSearchLDM savedSearch = new SavedSearchLDM();

	private OWLNamedObjectLDM<OWLClass> currentClass;

	public FilterPage(PageParameters parameters) {
		super(parameters);

		// moje da se konfigurira chrez parametri
		if (parameters.getNamedKeys().contains(USER_NAME_PARAM) && parameters.get(USER_NAME_PARAM).toOptionalString() != null) {
			Users u = UsersHome.findByUsernameOrEmail(ScopedEntityManagerFactory.getEntityManager(), parameters.get(USER_NAME_PARAM).toOptionalString());
			if (u != null) {
				UserSession.get().setUserId(u.getUsersId());
				if (parameters.getNamedKeys().contains(SESSION_NAME_PARAM) && parameters.get(SESSION_NAME_PARAM).toOptionalString() != null) {
					UserSession.get().setSearchSessionId(parameters.get(SESSION_NAME_PARAM).toOptionalString());
					if (parameters.getNamedKeys().contains(URIS_PARAM)) {
						Set<String> uris = new HashSet<String>();
						for (StringValue sv  : parameters.getValues(URIS_PARAM)) {
							if (!StringUtils.isEmpty(sv.toOptionalString())) {
								uris.add(sv.toOptionalString());
							}
						}

						SelectedResultsFilter filter = new SelectedResultsFilter();
						filter.setUserId(UserSession.get().getUserId());
						UserSession.get().setSearchSessionId(parameters.get(SESSION_NAME_PARAM).toOptionalString());
						filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, parameters.get(SESSION_NAME_PARAM).toOptionalString()));

						List<SelectedResults> results = SelectedResultsHome.getSelectedResults(ScopedEntityManagerFactory.getEntityManager(), filter, null);
						Collection<Object> toRemove = new ArrayList<Object>();
						for (SelectedResults sr : results) {
							if (!uris.contains(sr.getObjectIri())) {
								toRemove.add(sr);
							}
						}

						if (toRemove.size() > 0) {
							Home.removeCollectionInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), toRemove);
						}
 					}
				}
			}
		}

		savedSearch = new SavedSearchLDM();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IRI classIRI = OWLRDFVocabulary.OWL_THING.getIRI();
		OWLClass clazz = OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLClass(classIRI);

		CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
		cache.put(classIRI.toURI(), clazz);

        OWLOntologiesLDM ontologiesLDM = new OWLOntologiesLDM(new HashSet<OWLOntology>(new AllOntologiesLDM(true).getObject()));
        setDefaultModel(ontologiesLDM);

        // izbor na ontologii
        add(new SelectOntologiesPanel("select_ontolgoies", ontologiesLDM));

        // tekushta sesiq za tyrsene
		add(new AjaxLink<Void>("selected_session") {

			private static final long serialVersionUID = 4514481768461424700L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				UserSession.get().setSearchSessionId(null);
                ModalWindow modal = getModalWindow();
                modal.setTitle(new StringResourceModel("choose_session", FilterPage.this, null));
				SavedSearchesFilter filter = new SavedSearchesFilter();
				filter.setUserId(UserSession.get().getUserId());
                modal.setContent(new SelectSessionPanel(modal.getContentId(), new Model<SavedSearchesFilter>(filter)));
                modal.show(target);
			}

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof SessionSelectedEP) {
					((SessionSelectedEP) event.getPayload()).getTarget().add(this);
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(	UserSession.get().getUserId() != null &&
							!StringUtils.isEmpty(UserSession.get().getSearchSessionId()) &&
							!UserSession.get().isEmbedded());
				setBody(new Model<String>(UserSession.get().getSearchSessionId()));
			}
		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		// bazovi obekti za filtyr
        add(new RootObjectsPanel("root_objects", new DefaultFilterClassesLDM(ontologiesLDM), ontologiesLDM));

        // forma za filtyr
        Form<Set<OWLOntology>> filterForm = new Form<Set<OWLOntology>>(FORM_ID, ontologiesLDM) {

            private static final long serialVersionUID = 1738606701937800061L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RootNodeSelectedEP) {
					RootNodeSelectedEP payload = (RootNodeSelectedEP) event.getPayload();
					currentClass = new OWLNamedObjectLDM<OWLClass>(payload.getSelectedClass().getObject());
					addOrReplaceProperties(this);
				} else if (event.getPayload() instanceof SessionSelectedEP) {
					addOrReplaceSavedSearch();
				} else if (event.getPayload() instanceof OntologiesSelectedEP) {
					currentClass = new OWLNamedObjectLDM<OWLClass>();
					savedSearch = new SavedSearchLDM();
					addOrReplaceProperties(this);
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);

		// pokazva tyrseneto - SPARQL + Human readable
		final ShowSearchButton showSearch = new ShowSearchButton("show_search");
		filterForm.add(showSearch);
		filterForm.add(new IndicatingAjaxButton("submit_search") {

            private static final long serialVersionUID = -2448719880318808311L;

            @Override
            protected void onInitialize() {
            	super.onInitialize();
            	setOutputMarkupId(true);
            	setOutputMarkupPlaceholderTag(true);
            }

            @Override
            protected void onConfigure() {
            	super.onConfigure();
            	showSearch.configure();
            	setVisible(showSearch.isVisible());
            }

            @Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				OWLClassFilterDisplayPanel filterPanel = getFilterPanel();
				if (filterPanel != null) {
					SavedSearches search = new SavedSearches();
					if (savedSearch != null && savedSearch.getObject() != null && savedSearch.getSearchResultsModel().getObject().size() > 0) {
						search.setSavedSearches(savedSearch.getObject());
						search.setSparql(QueryUtil.createFullFilterQueryString(filterPanel, savedSearch.getSearchResultsModel().getObject()));
					} else {
						search.setSparql(QueryUtil.createFullFilterQueryString(filterPanel));
					}

					search.setSparqlNoContext(QueryUtil.createFullFilterQueryString(filterPanel));

					search.setObjectUri(currentClass.getObject().getIRI().toString());

					ViewClassFilterLabel humanReadable = new ViewClassFilterLabel("human_readable") {

						private static final long serialVersionUID = 9191152383591682281L;

						@Override
						protected OWLClassFilterDisplayPanel getFilterPanel() {
							return FilterPage.this.getFilterPanel();
						}
					};
					search.setHumanReadable(humanReadable.getDefaultModelObjectAsString());

					Iterator<IModel<OWLNamedObject>> it = getDisplayPanel().getDisplayView().getItemModels();

					while (it.hasNext()) {
						SearchDisplayValues sdv = new SearchDisplayValues();
						sdv.setUriPath((DefaultDisplayValuesHome.getPath(it.next(), currentClass.getObject())));
						search.getSearchDisplayValueses().add(sdv);
					}

					setResponsePage(new SearchResultsPage(new Model<SavedSearches>(search)));
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});

		addOrReplaceProperties(filterForm);

		// tuk se pokazva prozoreca za izbor na sesiq
		if (UserSession.get().getUserId() != null && StringUtils.isEmpty(UserSession.get().getSearchSessionId())) {
			add(new AbstractDefaultAjaxBehavior() {

                private static final long serialVersionUID = -8388028346524251607L;

				@Override
                protected void respond(AjaxRequestTarget target) {
	                ModalWindow modal = getModalWindow();
	                modal.setTitle(new StringResourceModel("choose_session", FilterPage.this, null));
					SavedSearchesFilter filter = new SavedSearchesFilter();
					filter.setUserId(UserSession.get().getUserId());
	                modal.setContent(new SelectSessionPanel(modal.getContentId(), new Model<SavedSearchesFilter>(filter)));
	                modal.show(target);
                }

				@Override
				public void renderHead(Component component, IHeaderResponse response) {
					response.renderOnLoadJavaScript(getCallbackScript().toString());
				}
			});
		}
	}

	/**
	 * komponenti za filtyr i stoinosti za pokazvane
	 * @param parent
	 */
	private void addOrReplaceProperties(MarkupContainer parent) {
		if (currentClass != null && currentClass.getObject() != null) {
			OWLOntologiesLDM ontologiesLDM = (OWLOntologiesLDM) getDefaultModel();
			OWLHierarchyNamedObjectLDM<OWLClass> hierarchy = new OWLHierarchyNamedObjectLDM<OWLClass>(currentClass.getObject(), null);
			parent.addOrReplace(new OWLClassFilterDisplayPanel(PROPERTIES_ID, hierarchy, ontologiesLDM).setOutputMarkupId(true));
			parent.addOrReplace(new OWLClassDisplayPanel(PROPERTIES_DISPLAY_ID, hierarchy, ontologiesLDM) {

                private static final long serialVersionUID = 1L;

				@Override
                protected List<String> getPaths(OWLClass owlClass) {
	                List<String> result = new ArrayList<String>();
	                if (owlClass != null) {
	                	List<DefaultDisplayValuesPath> paths = DefaultDisplayValuesHome.getDefaultDisplayValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClass, ontologies.getObject());
	                	for (DefaultDisplayValuesPath ddvp : paths) {
	                		result.add(ddvp.getPath());
	                	}
	                }

	                return result;
                }}.setOutputMarkupId(true));
		} else {
			parent.addOrReplace(new EmptyPanel(PROPERTIES_ID).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setVisible(false));
			parent.addOrReplace(new EmptyPanel(PROPERTIES_DISPLAY_ID).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setVisible(false));
		}

		addOrReplaceSavedSearch();

		if (AjaxRequestTarget.get() != null) {
			AjaxRequestTarget.get().add(parent);
			DojoUtils.refreshWidgets(AjaxRequestTarget.get(), parent);
		}
	}

	/**
	 * dobavqne na komponent za izbor na kontext (ili veche izbran kontext)
	 */
	private void addOrReplaceSavedSearch() {
		Component c = null;
		if (currentClass != null && currentClass.getObject() != null) {
			if (savedSearch != null && savedSearch.getObject() != null) {
				c = new SavedSearchFragment(SAVED_SEARCHES_ID, "selected_saved_search_fragment", FilterPage.this);
			} else if (UserSession.get().getSearchSessionId() != null) {
				SavedSearchesFilter filter = new SavedSearchesFilter();
				filter.setObjectUri(currentClass.getObject().getIRI().toString());
				filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, UserSession.get().getSearchSessionId()));
				c = new SavedSearchesFragment(SAVED_SEARCHES_ID, "saved_searches_fragment", this, new Model<SavedSearchesFilter>(filter)) {

                    private static final long serialVersionUID = -2149588680603410182L;

					@Override
			        protected void onConfigure() {
			        	super.onConfigure();
			        	setVisible(UserSession.get().getSearchSessionId() != null && UserSession.get().getUserId() != null && (savedSearch == null || savedSearch.getObject() == null));
			        }
				};
			} else {
				c = new EmptyPanel(SAVED_SEARCHES_ID).setVisible(false);
			}
		} else {
			c = new EmptyPanel(SAVED_SEARCHES_ID).setVisible(false);
		}

		addOrReplace(c.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
		if (AjaxRequestTarget.get() != null) {
			AjaxRequestTarget.get().add(c);
		}
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("filter", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("filter", this, null);
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavaScript("dojo.require('dojo.fx');", "dojo.fx");
	}

	private class ShowSearchButton extends AjaxButton {

		private static final long serialVersionUID = -5342788631513840144L;

		public ShowSearchButton(String id) {
	        super(id);
        }

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			OWLClassFilterDisplayPanel c = getFilterPanel();
			if (c != null) {
				ModalWindow mw = findParent(IModalWindowContainer.class).getModalWindow();
				ViewClassFilterLabel humanReadable = new ViewClassFilterLabel("human_readable") {

					private static final long serialVersionUID = 9191152383591682281L;

					@Override
					protected OWLClassFilterDisplayPanel getFilterPanel() {
						return FilterPage.this.getFilterPanel();
					}
				};
				ViewSPARQLLabel sparql = new ViewSPARQLLabel("sparql", c, savedSearch.getSearchResultsModel());
				ComponentsFragment content = new ComponentsFragment(mw.getContentId(), "view_filter_fragment", FilterPage.this, humanReadable, sparql);
				content.add(AttributeModifier.append("style", "width: 1000px; height: 700px;"));
				mw.setContent(content);
				mw.setTitle(new StringResourceModel("view_filter", this, null));
				mw.show(target);
			}
		}

		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
		}

		@Override
		protected void onConfigure() {
			super.onConfigure();
			setVisible(currentClass != null && currentClass.getObject() != null);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			setOutputMarkupPlaceholderTag(true);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof SearchSelectedEP) {
			SearchSelectedEP payload = (SearchSelectedEP) event.getPayload();
			savedSearch = new SavedSearchLDM(payload.getSearch());
			addOrReplaceSavedSearch();
		} else if (event.getPayload() instanceof SessionSelectedEP) {
			getModalWindow().close(((SessionSelectedEP) event.getPayload()).getTarget());
		}
	}

	/**
	 * zapomneno tyrsene
	 *
	 * @author hok
	 *
	 */
	protected class SavedSearchFragment extends Fragment {

        private static final long serialVersionUID = -8697245564491583584L;

        public SavedSearchFragment(String id, String markupId, MarkupContainer markupProvider) {
	        super(id, markupId, markupProvider);
        }

		@Override
		protected void onInitialize() {
			super.onInitialize();

			final MultiLineLabel savedSearchTitle = new MultiLineLabel("saved_search_title") {

                private static final long serialVersionUID = -1474120931422564776L;

				@Override
				protected void onConfigure() {
					super.onConfigure();
					if (savedSearch != null && savedSearch.getObject() != null) {
						setDefaultModel(new HumanReadableLDM(savedSearch));
						setVisible(true);
					} else {
						setDefaultModel(null);
						setVisible(false);
					}
				}
			};
			savedSearchTitle.setEscapeModelStrings(false);
			savedSearchTitle.setOutputMarkupId(true);
			savedSearchTitle.setOutputMarkupPlaceholderTag(true);
			add(savedSearchTitle);

			RepeatingView rv = new RepeatingView("titles");
			add(rv);

			@SuppressWarnings("unchecked")
            IModel<Set<OWLOntology>> ontologies = (IModel<Set<OWLOntology>>) FilterPage.this.getDefaultModel();
			for (SearchDisplayValues sdv : savedSearch.getObject().getSearchDisplayValueses()) {
				String[] s = sdv.getUriPath().split(",");
				String title = null;
				if (s.length == 1) {
					title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
				} else {
					title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 2]).toURI().toString(), ontologies.getObject()), ontologies) + " -> " + OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
				}
				rv.add(new Label(rv.newChildId(), title));
			}

			DataView<OWLNamedObjectResult<String>> dataView = new SearchResultsDataView<String>("results", savedSearch.getObject().getObjectUri(), new SearchDataProvider(savedSearch, ontologies), 5);
			add(dataView);

			add(new AjaxPagingNavigator("paging", dataView));

			final AjaxLink<Void> clearSearch = new AjaxLink<Void>("clear_search") {

				private static final long serialVersionUID = -5167598503606073001L;

				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisible(savedSearch != null && savedSearch.getObject() != null);
				}

				@Override
				public void onClick(AjaxRequestTarget target) {
					savedSearch.setObject(null);
					send(getPage(), Broadcast.BREADTH, new SearchSelectedEP(target, null));
				}
			};
			clearSearch.setOutputMarkupId(true);
			clearSearch.setOutputMarkupPlaceholderTag(true);
			add(clearSearch);
		}
	}

	private OWLClassFilterDisplayPanel getFilterPanel() {
		if (get(FORM_ID + ":" + PROPERTIES_ID) instanceof OWLClassFilterDisplayPanel) {
			return (OWLClassFilterDisplayPanel) get(FORM_ID + ":" + PROPERTIES_ID);
		}

		return null;
	}

	private OWLClassDisplayPanel getDisplayPanel() {
		if (get(FORM_ID + ":" + PROPERTIES_DISPLAY_ID) instanceof OWLClassDisplayPanel) {
			return (OWLClassDisplayPanel) get(FORM_ID + ":" + PROPERTIES_DISPLAY_ID);
		}

		return null;
	}
}