package bg.bas.iinf.sinus.wicket.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.hibernate.dao.DefaultDisplayValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.DefaultFilterValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.entity.DefaultDisplayValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;
import bg.bas.iinf.sinus.hibernate.entity.Ontologies;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologyLDM;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel.RootNodeSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.filter.display.OWLClassDisplayPanel;

/**
 * Stranica za upravlenie na edinichna ontologiq
 *
 * @author hok
 *
 */
@AuthorizeInstantiation({AuthRoles.LIBRARIAN_CONST, AuthRoles.ADMIN_CONST})
public class OntologyConfigurationPage extends BaseAdminPage {

    private static final long serialVersionUID = -5191384697198658963L;

	public static final int ONTOLOGY_PARAM = 0;

	public OntologyConfigurationPage(PageParameters parameters) {
		super(parameters);

		setDefaultModel(new OWLOntologyLDM(getOntology().getId()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("title", this, null);
	}

	@SuppressWarnings("unchecked")
    @Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("ontology_title", new PropertyModel<String>(getDefaultModel(), "ontologyID.ontologyIRI")));

		add(new AjaxCheckBox("is_configured", new Model<Boolean>(getOntology().getIsConfigured())) {

            private static final long serialVersionUID = 7783719434473399924L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Ontologies o = getOntology();
				o.setIsConfigured(getConvertedInput());
				Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), o);
				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
			}
		});

		final IModel<Set<OWLOntology>> ontologies = new LoadableDetachableModel<Set<OWLOntology>>() {

            private static final long serialVersionUID = 1L;

			@Override
			protected Set<OWLOntology> load() {
				Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
				ontologies.add((OWLOntology) OntologyConfigurationPage.this.getDefaultModelObject());
				return ontologies;
			}
		};
		add(new RootObjectsPanel("root_objects", new RootClassesLDM((IModel<OWLOntology>) getDefaultModel()), ontologies));

		IModel<List<OWLClass>> defaultFilterValuesLDM = new LoadableDetachableModel<List<OWLClass>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<OWLClass> load() {
				OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
				List<DefaultFilterValues> dfvs = DefaultFilterValuesHome.getDefaultFilterValues(ScopedEntityManagerFactory.getEntityManager(), ontologies.getObject());

				List<OWLClass> owlClasses = new ArrayList<OWLClass>();
				for (DefaultFilterValues ddv : dfvs) {
					owlClasses.add(factory.getOWLClass(IRI.create(ddv.getIri())));
				}

				return owlClasses;
			}
		};
		final WebMarkupContainer defaultFilterValuesContainer = new WebMarkupContainer("default_filter_values_container", defaultFilterValuesLDM) {

            private static final long serialVersionUID = 9072376529144605760L;

            @Override
            protected void onConfigure() {
            	super.onConfigure();
            	IModel<List<?>> defaultFilterValuesLDM = (IModel<List<?>>) getDefaultModel();
            	setVisible(defaultFilterValuesLDM.getObject().size() > 0);
            }

        	@Override
        	public void onEvent(IEvent<?> event) {
        		if (event.getPayload() instanceof OWLFilterClassRemovedEP) {
        			((OWLFilterClassRemovedEP) event.getPayload()).getTarget().add(this);
        		} else if (event.getPayload() instanceof RootNodeSelectedEP) {
        			RootNodeSelectedEP payload = (RootNodeSelectedEP) event.getPayload();
        			if (DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), payload.getSelectedClass().getObject().getIRI().toString()) == null) {
        				DefaultFilterValues ddv = new DefaultFilterValues();
        				ddv.setIri(payload.getSelectedClass().getObject().getIRI().toString());
        				Integer id = getPageParameters().get(ONTOLOGY_PARAM).toInteger();
        				ddv.setOntologies(Home.findById(ScopedEntityManagerFactory.getEntityManager(), id, Ontologies.class));
        				Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), ddv);
        				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(payload.getTarget(), new ResourceModel("save_success"), NotificationType.INFO));
        				payload.getTarget().add(this);
        			}
        		}
        	}
		};
		defaultFilterValuesContainer.setOutputMarkupId(true);
		defaultFilterValuesContainer.setOutputMarkupPlaceholderTag(true);
		add(defaultFilterValuesContainer);

		defaultFilterValuesContainer.add(new RefreshingView<OWLClass>("default_filter_values", defaultFilterValuesLDM) {

            private static final long serialVersionUID = -5413176601011662706L;

			@Override
            protected Iterator<IModel<OWLClass>> getItemModels() {
	            List<IModel<OWLClass>> result = new ArrayList<IModel<OWLClass>>();
	            IModel<List<OWLClass>> defaultFilterValuesLDM = (IModel<List<OWLClass>>) getDefaultModel();
	            for (OWLClass c : defaultFilterValuesLDM.getObject()) {
	            	result.add(new OWLNamedObjectLDM<OWLClass>(c));
	            }

	            return result.iterator();
            }

			@Override
            protected void populateItem(Item<OWLClass> item) {
				item.add(new IndicatingAjaxLink<OWLClass>("select_class", item.getModel()) {

					private static final long serialVersionUID = -3275882424586350099L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						send(getPage(), Broadcast.BREADTH, new OWLFilterClassSelectedEP(target, getModel()));
					}

					@Override
					protected void onInitialize() {
						super.onInitialize();
						setBody(new OWLNamedObjectNameLDM<OWLClass>(getModel(), ontologies));
					}
				});

				item.add(new AjaxLink<OWLClass>("remove_link", item.getModel()) {

                    private static final long serialVersionUID = -2271878983695564147L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						EntityManager em = ScopedEntityManagerFactory.getEntityManager();
						Home.removeInOneTransaction(em, DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), getModelObject().getIRI().toString()));
						send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
						send(getPage(), Broadcast.BREADTH, new OWLFilterClassRemovedEP(target, getModel()));
					}
				});
            }
		});

		// default display values
		final WebMarkupContainer defaultValuesContainer = new WebMarkupContainer("default_values_container") {

            private static final long serialVersionUID = -5580258119104309492L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}

        	@Override
        	public void onEvent(IEvent<?> event) {
        		if (event.getPayload() instanceof OWLFilterClassRemovedEP) {
        			OWLFilterClassRemovedEP payload = (OWLFilterClassRemovedEP) event.getPayload();
        			OWLClass c = (OWLClass) getDefaultModelObject();
        			if (c != null && payload.getOwlClass().getObject().getIRI().equals(c.getIRI())) {
        				setDefaultModel(null);
        				replace(new EmptyPanel("default_display_values").setVisible(false));
            			payload.getTarget().add(this);
        			}
        		} else if (event.getPayload() instanceof OWLFilterClassSelectedEP) {
        			OWLFilterClassSelectedEP payload = (OWLFilterClassSelectedEP) event.getPayload();
        			setDefaultModel(payload.getOwlClass());

        			replace(new OWLClassDisplayPanel("default_display_values", payload.getOwlClass(), ontologies) {

                        private static final long serialVersionUID = 2337830505058457292L;

						@Override
        			    protected void propertiesAdded(AjaxRequestTarget target, List<OWLHierarchyNamedObjectLDM<OWLNamedObject>> displayProperties) {
							DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), getModelObject().getIRI().toString());
							dfv.getDefaultDisplayValuesPaths().clear();
							dfv.getDefaultDisplayValuesPaths().addAll(DefaultDisplayValuesHome.getPath(getDisplayView().getItemModels(), getModelObject()));
							for (DefaultDisplayValuesPath ddvp: dfv.getDefaultDisplayValuesPaths()) {
								ddvp.setDefaultFilterValues(dfv);
							}

							Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), dfv);
							send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
        				}

        				@Override
        				protected void propertyRemoved(AjaxRequestTarget target, OWLHierarchyNamedObjectLDM<?> model) {
        					DefaultDisplayValuesPath ddvp = DefaultDisplayValuesHome.findByPath(ScopedEntityManagerFactory.getEntityManager(), DefaultDisplayValuesHome.getPath(model, getModelObject()));
							DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), getModelObject().getIRI().toString());
							dfv.getDefaultDisplayValuesPaths().remove(ddvp);
							Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), dfv);

							send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
        				}

        				@Override
                        protected List<String> getPaths(OWLClass owlClass) {
        	                List<String> result = new ArrayList<String>();
        	                if (owlClass != null) {
        	                	DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), owlClass.getIRI().toString());
        	                	if (dfv != null) {
	        	                	for (DefaultDisplayValuesPath ddvp : dfv.getDefaultDisplayValuesPaths()) {
	        	                		result.add(ddvp.getPath());
	        	                	}
        	                	}
        	                }

        	                return result;
                        }
        			});

        			payload.getTarget().add(this);
        			DojoUtils.refreshWidgets(payload.getTarget(), this);
        		}
        	}
		};
		defaultValuesContainer.setOutputMarkupId(true);
		defaultValuesContainer.setOutputMarkupPlaceholderTag(true);
		add(defaultValuesContainer);
		defaultValuesContainer.add(new EmptyPanel("default_display_values").setVisible(false));
	}

	private Ontologies getOntology() {
		Integer id = getPageParameters().get(ONTOLOGY_PARAM).toInteger();
		return Home.findById(ScopedEntityManagerFactory.getEntityManager(), id, Ontologies.class);
	}

	private static class OWLFilterClassSelectedEP {
		private AjaxRequestTarget target;
		private IModel<OWLClass> owlClass;

		public OWLFilterClassSelectedEP(AjaxRequestTarget target, IModel<OWLClass> owlClass) {
			super();
			this.target = target;
			this.owlClass = owlClass;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public IModel<OWLClass> getOwlClass() {
			return owlClass;
		}
	}

	private static class OWLFilterClassRemovedEP {
		private AjaxRequestTarget target;
		private IModel<OWLClass> owlClass;

		public OWLFilterClassRemovedEP(AjaxRequestTarget target, IModel<OWLClass> owlClass) {
			super();
			this.target = target;
			this.owlClass = owlClass;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public IModel<OWLClass> getOwlClass() {
			return owlClass;
		}
	}

	private static class RootClassesLDM extends LoadableDetachableModel<List<OWLClass>> {

        private static final long serialVersionUID = 5535609765381820847L;

        private IModel<OWLOntology> ontology;

		public RootClassesLDM(IModel<OWLOntology> ontology) {
	        super();
	        this.ontology = ontology;
        }

		@Override
		protected void onDetach() {
			super.onDetach();
			ontology.detach();
		}

		@Override
        protected List<OWLClass> load() {
			if (ontology.getObject() == null) {
				return Collections.<OWLClass>emptyList();
			}

			OWLClass thing = OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLClass(OWLRDFVocabulary.OWL_THING.getIRI());

			List<OWLClass> result = new ArrayList<OWLClass>();
			for (OWLClass root : ReasonerResolver.getReasoner(ontology.getObject()).getSubClasses(thing, true).getFlattened()) {
				if (root.getIRI().getStart().startsWith(ontology.getObject().getOntologyID().getOntologyIRI().toURI().toString())) {
					result.add(root);
				}
			}

			return result;
		}
	}
}
