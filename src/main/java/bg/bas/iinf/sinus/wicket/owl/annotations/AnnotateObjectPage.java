package bg.bas.iinf.sinus.wicket.owl.annotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.UrlValidator;
import org.openrdf.query.Binding;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;

import bg.bas.iinf.sinus.hibernate.dao.DefaultAnnotateValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.DefaultDisplayValuesHome;
import bg.bas.iinf.sinus.hibernate.entity.DefaultAnnotateValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.DefaultDisplayValuesPath;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.repository.QueryUtil;
import bg.bas.iinf.sinus.repository.RepoUtil;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.model.DefaultAnnotateValuesPathLDM;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.owl.SelectOntologiesPanel;
import bg.bas.iinf.sinus.wicket.owl.SelectOntologiesPanel.OntologiesSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.ViewObjectPage;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.LiftingDataProvider;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView.OWLNamedObjectResult;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * Stranica za anotirane na obekt
 * @author hok
 *
 */
@AuthorizeInstantiation(AuthRoles.LIBRARIAN_CONST)
public class AnnotateObjectPage extends BasePage {

    private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(AnnotateObjectPage.class);

    public static String PARAMETER_CLASS = "class";
    public static String PARAMETER_URI = "uri";

    private IModel<OWLClass> owlClassLDM;
    private IModel<Set<OWLOntology>> ontologies;
    private IModel<Set<OWLOntology>> allOntologies;

    public AnnotateObjectPage(PageParameters parameters) {
	    super(parameters);
    }

    @Override
    protected void onDetach() {
    	super.onDetach();
    	owlClassLDM.detach();
    	ontologies.detach();
    	allOntologies.detach();
    }

    @Override
	protected void onInitialize() {
		super.onInitialize();

		OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
		owlClassLDM = new OWLNamedObjectLDM<OWLClass>(factory.getOWLClass(IRI.create(getPageParameters().get(PARAMETER_CLASS).toString())));

		allOntologies = new LoadableDetachableModel<Set<OWLOntology>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected Set<OWLOntology> load() {
				return new HashSet<OWLOntology>(new AllOntologiesLDM(true).getObject());
			}
		};

        add(new BookmarkablePageLink<ViewObjectPage>("view_object", ViewObjectPage.class, getPageParameters()).setBody(new Model<String>(getPageParameters().get(PARAMETER_URI).toString())));

		add(new RefreshingView<ObjectTuple>("properties") {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<IModel<ObjectTuple>> getItemModels() {
				SelectedResultsDataProvider dataProvider = new SelectedResultsDataProvider(allOntologies);
				OWLNamedObjectResult<Integer> object = dataProvider.iterator(0, 1).next();

				List<DefaultDisplayValuesPath> paths = DefaultDisplayValuesHome.getDefaultDisplayValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClassLDM.getObject(), ontologies.getObject());
				if (object.getProperties().size() == paths.size()) {
					List<IModel<ObjectTuple>> result = new ArrayList<IModel<ObjectTuple>>();
					for (int i = 0; i < object.getProperties().size(); i++) {
						String[] s = paths.get(i).getPath().split(",");
						String title = null;
						if (s.length == 1) {
							title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), allOntologies.getObject()), allOntologies);
						} else {
							title = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 2]).toURI().toString(), allOntologies.getObject()), allOntologies) + " -> " + OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(s[s.length - 1]).toURI().toString(), ontologies.getObject()), ontologies);
						}

						result.add(new Model<ObjectTuple>(new ObjectTuple(title, object.getProperties().get(i))));
					}

					return result.iterator();
				}

				return Collections.<IModel<ObjectTuple>>emptyList().iterator();
			}

			@Override
			protected void populateItem(final Item<ObjectTuple> item) {
				item.add(new Label("key", new PropertyModel<String>(item.getModel(), "key")));
				item.add(new Label("value", new LoadableDetachableModel<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					protected String load() {
						UrlValidator urlValidator = new UrlValidator();
						String result = null;
						String s = item.getModelObject().getValue();
						if (urlValidator.isValid(s)) {
							if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")) {
								result = "<img src=\"" + s + "\" />";
							} else {
								result = "<a href=\"" + s + "\">" + s + "</a>";
							}
						} else {
							result = s;
						}

						return result;
					}
				}).setEscapeModelStrings(false));
			}
		});

		ontologies = new LoadableDetachableModel<Set<OWLOntology>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected Set<OWLOntology> load() {
				return new HashSet<OWLOntology>(allOntologies.getObject());
			}
		};
		add(new SelectOntologiesPanel("select_ontolgoies", ontologies));

        Form<Void> annotationsForm = new Form<Void>("annotations_form") {

            private static final long serialVersionUID = 1L;

        	@Override
        	public void onEvent(IEvent<?> event) {
        		if (event.getPayload() instanceof OntologiesSelectedEP) {
        			OntologiesSelectedEP payload = (OntologiesSelectedEP) event.getPayload();
        			addOrReplaceAnnotations(this);
        			DojoUtils.refreshWidgets(payload.getTarget(), this);
        			payload.getTarget().add(this);
        		}
        	}
        };
        add(annotationsForm.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
        addOrReplaceAnnotations(annotationsForm);

		annotationsForm.add(new IndicatingAjaxButton("submit") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				form.visitChildren(OWLIndividualsDropDownChoice.class, new IVisitor<OWLIndividualsDropDownChoice, Void>() {

					@Override
					public void component(OWLIndividualsDropDownChoice object, IVisit<Void> visit) {
						if (object.getModelObject() != null) {
							DefaultAnnotateValuesPath p = (DefaultAnnotateValuesPath) object.getParent().getDefaultModelObject();
							List<String> paths = new ArrayList<String>(Arrays.asList(p.getPath().split(",")));
							Collections.reverse(paths);

							// syzdavane na pyt
							List<OWLNamedObject> chain = new ArrayList<OWLNamedObject>();
							for (String path : paths) {
								OWLNamedObject o = OWLOntologiesLDM.getOWLNamedObject(IRI.create(path).toURI().toString(), allOntologies.getObject());
								if 	(!(o instanceof OWLClass)) {
									chain.add(o);
								}
							}
							chain.add(owlClassLDM.getObject());
							Collections.reverse(chain);

							RepoUtil.executeSPARQLQuery(QueryUtil.createUpdateValueQuery(chain, getPageParameters().get(PARAMETER_URI).toString(), object.getModelObject().getIRI().toString()));
						}
					}
				});

				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
				target.add(form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
	}

	private void addOrReplaceAnnotations(MarkupContainer parent) {
        parent.addOrReplace(new RefreshingView<DefaultAnnotateValuesPath>("annotations") {

            private static final long serialVersionUID = 5847912477409356567L;

			@Override
			protected Iterator<IModel<DefaultAnnotateValuesPath>> getItemModels() {
				List<IModel<DefaultAnnotateValuesPath>> result = new ArrayList<IModel<DefaultAnnotateValuesPath>>();
				List<DefaultAnnotateValuesPath> paths = DefaultAnnotateValuesHome.getDefaultAnnotateValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClassLDM.getObject(), ontologies.getObject());
				skip:
				for (DefaultAnnotateValuesPath davp : paths) {
					for (String iri : davp.getPath().split(",")) {
						boolean included = false;
						for (OWLOntology o : ontologies.getObject()) {
							if (iri.contains(o.getOntologyID().getOntologyIRI().toString())) {
								included = true;
								break;
							}
						}

						if (!included) {
							continue skip;
						}
					}

					result.add(new DefaultAnnotateValuesPathLDM(davp));
				}

				return result.iterator();
			}

			@Override
			protected void populateItem(final Item<DefaultAnnotateValuesPath> item) {
				IModel<String> path = new LoadableDetachableModel<String>() {

					private static final long serialVersionUID = 1L;

					@Override
					protected String load() {
						List<String> names = new ArrayList<String>();
						for (String iri : item.getModelObject().getPath().split(",")) {
							OWLNamedObject o = OWLOntologiesLDM.getOWLNamedObject(IRI.create(iri).toURI().toString(), allOntologies.getObject());
							if (!(o instanceof OWLClass)) {
								names.add(OWLNamedObjectNameLDM.load(o, allOntologies));
							}
						}

						return StringUtils.join(names, " -> ");
					}
				};

				item.add(new Label("name", path));

				OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
				List<String> paths = new ArrayList<String>(Arrays.asList(item.getModelObject().getPath().split(",")));
				Collections.reverse(paths);
				OWLClass owlClass = factory.getOWLClass(IRI.create(paths.get(0)));

				// proverka dali ima takyv individ v hranilishteto
				OWLNamedIndividual selectedAnnotation = null;
				List<OWLNamedObject> chain = new ArrayList<OWLNamedObject>();
				for (String p : paths) {
					OWLNamedObject o = OWLOntologiesLDM.getOWLNamedObject(IRI.create(p).toURI().toString(), allOntologies.getObject());
					if 	(!(o instanceof OWLClass)) {
						chain.add(o);
					}
				}
				chain.add(owlClassLDM.getObject());
				Collections.reverse(chain);

				TupleQueryResult tqr = RepoUtil.executeSPARQLQuery(QueryUtil.createSelectValueQuery(chain, getPageParameters().get(PARAMETER_URI).toString()));
				try {
					while (tqr.hasNext()) {
						Iterator<Binding> it = tqr.next().iterator();
						while (it.hasNext()) {
							String value = it.next().getValue().stringValue();
							for (OWLIndividual ind : owlClassLDM.getObject().getIndividuals(ontologies.getObject())) {
								if (ind instanceof OWLNamedIndividual) {
									OWLNamedIndividual namedInd = (OWLNamedIndividual) ind;
									if (namedInd.getIRI().toString().equals(value)) {
										selectedAnnotation = namedInd;
										break;
									}
								}
							}
						}
					}
				} catch (QueryEvaluationException e) {
					log.error(e);
				}

				item.add(new OWLIndividualsDropDownChoice("individuals", new OWLNamedObjectLDM<OWLNamedIndividual>(selectedAnnotation), new OWLNamedObjectLDM<OWLClass>(owlClass), ontologies));
			}
		});
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("title", this, null);
	}

	private static class OWLIndividualsDropDownChoice extends DropDownChoice<OWLNamedIndividual> {

		private static final long serialVersionUID = 1L;

		private IModel<OWLClass> owlClassLDM;
		private IModel<Set<OWLOntology>> ontologies;

		public OWLIndividualsDropDownChoice(String id, IModel<OWLNamedIndividual> model, IModel<OWLClass> owlClassLDM, IModel<Set<OWLOntology>> ontologies) {
			super(id);
			setModel(model);
			this.owlClassLDM = owlClassLDM;
			this.ontologies = ontologies;
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			setChoices(new LoadableDetachableModel<List<? extends OWLNamedIndividual>>() {

				private static final long serialVersionUID = 1L;

				@Override
				protected List<? extends OWLNamedIndividual> load() {
					List<OWLNamedIndividual> result = new ArrayList<OWLNamedIndividual>();
					Set<OWLIndividual> individuals = owlClassLDM.getObject().getIndividuals(ontologies.getObject());
					for (OWLIndividual ind : individuals) {
						result.add((OWLNamedIndividual) ind);
					}

					return result;
				}
			});

			setChoiceRenderer(new IChoiceRenderer<OWLNamedIndividual>() {

				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(OWLNamedIndividual object) {
					return OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(object.getIRI().toString(), ontologies.getObject()), ontologies);
				}

				@Override
				public String getIdValue(OWLNamedIndividual object, int index) {
					return object.getIRI().toString();
				}
			});
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			owlClassLDM.detach();
		}
	}

	@SuppressWarnings("unused")
	private static class ObjectTuple implements Serializable {

        private static final long serialVersionUID = 1L;

        private String key;
		private String value;

        public ObjectTuple(String key, String value) {
	        super();
	        this.key = key;
	        this.value = value;
        }

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	private class SelectedResultsDataProvider extends LiftingDataProvider<Integer> {

        private static final long serialVersionUID = -6564666897009173552L;

		public SelectedResultsDataProvider(IModel<Set<OWLOntology>> ontologies) {
	        super(ontologies);
        }

		@Override
		protected String getRootClassIRI() {
			return owlClassLDM.getObject().getIRI().toString();
		}

		@Override
		public List<String> getIndividualIRIs() {
			List<String> result = new ArrayList<String>();
			result.add(getPageParameters().get(PARAMETER_URI).toString());
			return result;
		}

		@Override
		protected List<List<String>> getDisplayPaths() {
			List<List<String>> result = new ArrayList<List<String>>();
			List<DefaultDisplayValuesPath> paths = DefaultDisplayValuesHome.getDefaultDisplayValuesPaths(ScopedEntityManagerFactory.getEntityManager(), owlClassLDM.getObject(), ontologies.getObject());
			for (DefaultDisplayValuesPath path : paths) {
				result.add(new ArrayList<String>(Arrays.asList(path.getPath().split(","))));
			}

			return result;
		}

		@Override
        protected Integer getTForUri(String uri) {
			return Integer.valueOf(uri);
		};
	}
}
