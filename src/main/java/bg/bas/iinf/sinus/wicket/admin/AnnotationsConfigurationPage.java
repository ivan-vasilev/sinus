package bg.bas.iinf.sinus.wicket.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.hibernate.dao.DefaultAnnotateValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.DefaultFilterValuesHome;
import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.entity.DefaultAnnotateValuesPath;
import bg.bas.iinf.sinus.hibernate.entity.DefaultFilterValues;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.model.DefaultFilterClassesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.owl.ChildPropertiesVisitor;
import bg.bas.iinf.sinus.wicket.owl.OWLHierarchicalMenu;
import bg.bas.iinf.sinus.wicket.owl.OWLHierarchicalMenu.ObjectSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel;
import bg.bas.iinf.sinus.wicket.owl.RootObjectsPanel.RootNodeSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.filter.display.OWLViewPropertiesDropDownButton;

/**
 * Stranica za konfigurirane na stoinostite za anotaciq
 *
 * @author hok
 *
 */
@AuthorizeInstantiation({ AuthRoles.LIBRARIAN_CONST, AuthRoles.ADMIN_CONST })
public class AnnotationsConfigurationPage extends BaseAdminPage {

	private static final long serialVersionUID = -5191384697198658963L;
	private static final String PROPERTIES_ID = "properties";

	private OWLNamedObjectLDM<OWLClass> currentClass;
	private IModel<Set<OWLOntology>> ontologies;

	public AnnotationsConfigurationPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onDetach() {
		super.onDetach();

		if (currentClass != null) {
			currentClass.detach();
		}

		ontologies.detach();
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		ontologies = new LoadableDetachableModel<Set<OWLOntology>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected Set<OWLOntology> load() {
				return new HashSet<OWLOntology>(new AllOntologiesLDM(true).getObject());
			}
		};
		add(new RootObjectsPanel("root_objects", new DefaultFilterClassesLDM(ontologies), ontologies));

		final WebMarkupContainer container = new WebMarkupContainer("container") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RootNodeSelectedEP) {
					RootNodeSelectedEP payload = (RootNodeSelectedEP) event.getPayload();
					currentClass = new OWLNamedObjectLDM<OWLClass>(payload.getSelectedClass().getObject());
					if (DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), currentClass.getObject().getIRI().toString()) == null) {
						send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(AjaxRequestTarget.get(), new StringResourceModel("no_dfv", this, null), NotificationType.ERROR));
					} else {
						addOrReplaceProperties(this);
					}
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(currentClass != null && currentClass.getObject() != null);
			}
		};

		add(container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
		addOrReplaceProperties(container);
	}

	/**
	 * komponenti za filtyr i stoinosti za pokazvane
	 *
	 * @param parent
	 */
	private void addOrReplaceProperties(MarkupContainer parent) {
		if (currentClass != null && currentClass.getObject() != null && DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), currentClass.getObject().getIRI().toString()) != null) {
			OWLHierarchyNamedObjectLDM<OWLClass> hierarchy = new OWLHierarchyNamedObjectLDM<OWLClass>(currentClass.getObject(), null);
			parent.addOrReplace(new OWLAnnotationsFragment(PROPERTIES_ID, "properties_fragment", this, hierarchy, ontologies).setOutputMarkupId(true));
		} else {
			parent.addOrReplace(new EmptyPanel(PROPERTIES_ID).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setVisible(false));
		}

		if (AjaxRequestTarget.get() != null) {
			AjaxRequestTarget.get().add(parent);
			DojoUtils.refreshWidgets(AjaxRequestTarget.get(), parent);
		}
	}

	/**
	 * komponenta za anotirane
	 *
	 * @author hok
	 *
	 */
	public static class OWLAnnotationsFragment extends Fragment {

		private static final long serialVersionUID = -5934569692173886963L;

		protected IModel<Set<OWLOntology>> ontologies;

		public OWLAnnotationsFragment(String id, String markupId, MarkupContainer markupProvider, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
			super(id, markupId, markupProvider, model);
			this.ontologies = ontologies;
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			@SuppressWarnings("unchecked")
			IModel<OWLClass> m = (IModel<OWLClass>) getDefaultModel();
			add(new OWLViewPropertiesDropDownButton("annotations_menu", m, ontologies) {

				private static final long serialVersionUID = 6086035707256198796L;

				@SuppressWarnings("unchecked")
				@Override
				protected void addMenu(String id) {
					add(new OWLAnnotationsMenu(id, (IModel<OWLClass>) getDefaultModel(), ontologies));
				}
			});

			WebMarkupContainer container = new WebMarkupContainer("annotation_values_container") {

				private static final long serialVersionUID = -1455335226228925254L;

				@Override
				public void onEvent(IEvent<?> event) {
					if (event.getPayload() instanceof ObjectSelectedEP) {
						((ObjectSelectedEP) event.getPayload()).getTarget().add(this);
					} else if (event.getPayload() instanceof AnnotationsEntryRemovedEP) {
						((AnnotationsEntryRemovedEP) event.getPayload()).getTarget().add(this);
					}
				}
			};
			container.setOutputMarkupId(true);
			add(container);

			container.add(new AnnotationsRefreshingView("annotation_values", m));
		}

		/**
		 * spisyk s izbrani property-ta za pokazvane - pokazvat se pylnite
		 * pytishta
		 *
		 * @author hok
		 *
		 */
		private class AnnotationsRefreshingView extends RefreshingView<OWLNamedObject> {

			private static final long serialVersionUID = -2387118731361524568L;

			private List<IModel<OWLNamedObject>> list = new ArrayList<IModel<OWLNamedObject>>();

			@SuppressWarnings("unchecked")
			public AnnotationsRefreshingView(String id, IModel<OWLClass> model) {
				super(id, model);

				list = new ArrayList<IModel<OWLNamedObject>>();

				List<String> paths = new ArrayList<String>();
				DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), model.getObject().getIRI().toString());
				for (DefaultAnnotateValuesPath ddvp : dfv.getDefaultAnnotateValuesPaths()) {
					paths.add(ddvp.getPath());
				}

				if (paths.size() > 0) {
					for (String path : paths) {
						IModel<? extends OWLNamedObject> prev = model;
						for (String uri : path.split(",")) {
							OWLHierarchyNamedObjectLDM<OWLNamedObject> m = new OWLHierarchyNamedObjectLDM<OWLNamedObject>(OWLOntologiesLDM.getOWLNamedObject(IRI.create(uri).toURI().toString(), ontologies.getObject()), prev);
							prev = m;
						}
						list.add((IModel<OWLNamedObject>) prev);
					}
				} else {
					OWLClass c = model.getObject();
					Set<OWLOntology> o = ontologies.getObject();
					ChildPropertiesVisitor propertiesVisitor = new ChildPropertiesVisitor(c, o);

					if (propertiesVisitor.getChildProperties().size() == 1) {
						OWLHierarchyNamedObjectLDM<OWLNamedObject> m = new OWLHierarchyNamedObjectLDM<OWLNamedObject>(propertiesVisitor.getChildProperties().iterator().next(), model);
						list.add(m);
					}
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(Item<OWLNamedObject> item) {
				OWLHierarchyNamedObjectLDM<OWLNamedObject> m = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) item.getModel();
				List<String> names = new LinkedList<String>();
				OWLHierarchyNamedObjectLDM<OWLNamedObject> currentM = m;
				do {
					if (!(currentM.getObject() instanceof OWLClass)) {
						names.add(OWLNamedObjectNameLDM.load(currentM.getObject(), ontologies));
					}

					if (currentM.getParent() instanceof OWLHierarchyNamedObjectLDM) {
						currentM = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) currentM.getParent();
					} else {
						currentM = null;
					}
				} while (currentM != null);
				Collections.reverse(names);

				item.add(new Label("name", StringUtils.join(names, " -> ")));
				item.add(new AjaxLink<OWLNamedObject>("remove", item.getModel()) {

					private static final long serialVersionUID = -2194302015975971565L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						list.remove(getModel());
						DefaultAnnotateValuesPath davp = DefaultAnnotateValuesHome.findByPath(ScopedEntityManagerFactory.getEntityManager(),
								DefaultAnnotateValuesHome.getPath(getModel(), (OWLClass) OWLAnnotationsFragment.this.getDefaultModelObject()));
						DefaultFilterValues dfv = DefaultFilterValuesHome.findByIRI(ScopedEntityManagerFactory.getEntityManager(), ((OWLClass) OWLAnnotationsFragment.this.getDefaultModelObject()).getIRI().toString());
						dfv.getDefaultAnnotateValuesPaths().remove(davp);
						Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), dfv);

						send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
						send(getPage(), Broadcast.BREADTH, new AnnotationsEntryRemovedEP(target));
					}
				});
			}

			@Override
			public Iterator<IModel<OWLNamedObject>> getItemModels() {
				return list.iterator();
			}

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof ObjectSelectedEP) {
					ObjectSelectedEP payload = (ObjectSelectedEP) event.getPayload();
					if (payload.getModel().getObject() instanceof OWLClass && ((OWLClass) payload.getModel().getObject()).getIndividuals(ontologies.getObject()).size() > 0) {
						if (DefaultAnnotateValuesHome.addPath(ScopedEntityManagerFactory.getEntityManager(), payload.getModel(), (OWLClass) OWLAnnotationsFragment.this.getDefaultModelObject())) {
							list.add(payload.getModel());
							send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(payload.getTarget(), new ResourceModel("save_success"), NotificationType.INFO));
						}
					} else {
						send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(payload.getTarget(), new StringResourceModel("select_correct_values", this, null), NotificationType.ERROR));
					}
				}
			}
		}

		public static class AnnotationsEntryRemovedEP {

			private AjaxRequestTarget target;

			public AnnotationsEntryRemovedEP(AjaxRequestTarget target) {
				super();
				this.target = target;
			}

			public AjaxRequestTarget getTarget() {
				return target;
			}
		}

		/**
		 * menu za stoinosti za anotirane
		 *
		 * @author hok
		 *
		 */
		private static class OWLAnnotationsMenu extends OWLHierarchicalMenu {

			private static final long serialVersionUID = 1L;

			public OWLAnnotationsMenu(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
				super(id, model, ontologies);
			}

			@Override
			protected Collection<? extends OWLNamedObject> getRootObjects() {
				OWLClass owlClass = (OWLClass) getDefaultModelObject();
				Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
				Set<OWLProperty<?, ?>> properties = new ChildPropertiesVisitor(owlClass, ontologies.getObject()).getChildProperties();
				for (OWLProperty<?, ?> prop : properties) {
					if (hasIndividuals(prop)) {
						result.add(prop);
					}
				}

				for (OWLOntology o : ontologies.getObject()) {
					OWLReasoner r = ReasonerResolver.getReasoner(o);
					for (OWLClass superClass : r.getSuperClasses(owlClass, true).getFlattened()) {
						properties = new ChildPropertiesVisitor(superClass, ontologies.getObject()).getChildProperties();
						for (OWLProperty<?, ?> prop : properties) {
							if (hasIndividuals(prop)) {
								result.add(prop);
							}
						}
					}
				}

				return result;
			}

			@Override
			protected Collection<? extends OWLNamedObject> getObjectChildren(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel) {
				Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
				OWLNamedObject parent = parentModel.getObject();

				if (parent instanceof OWLClass) {
					for (OWLClassExpression subclass : ((OWLClass) parent).getSubClasses(ontologies.getObject())) {
						for (OWLClass owlClass : subclass.getClassesInSignature()) {
							if (hasIndividuals(owlClass)) {
								result.add(owlClass);
							}
						}
					}

					Set<OWLProperty<?, ?>> properties = new ChildPropertiesVisitor((OWLClass) parent, ontologies.getObject()).getChildProperties();
					for (OWLProperty<?, ?> op : properties) {
						if (hasIndividuals(op)) {
							result.add(op);
						}
					}
				} else if (parent instanceof OWLObjectProperty) {
					OWLObjectProperty objectProperty = (OWLObjectProperty) parent;
					for (OWLClassExpression rangeEntry : objectProperty.getRanges(ontologies.getObject())) {
						for (OWLClass owlClass : rangeEntry.getClassesInSignature()) {
							if (hasIndividuals(owlClass)) {
								result.add(owlClass);
							}
						}
					}
				}

				return result;
			}

			private boolean hasIndividuals(OWLNamedObject object) {
				if (object instanceof OWLObjectProperty) {
					OWLObjectProperty objectProperty = (OWLObjectProperty) object;
					for (OWLClassExpression rangeEntry : objectProperty.getRanges(ontologies.getObject())) {
						for (OWLClass owlClass : rangeEntry.getClassesInSignature()) {
							if (hasIndividuals(owlClass)) {
								return true;
							}
						}
					}
				} else if (object instanceof OWLClass) {
					if (((OWLClass) object).getIndividuals(ontologies.getObject()).size() > 0) {
						return true;
					}

					Set<OWLProperty<?, ?>> properties = new ChildPropertiesVisitor((OWLClass) object, ontologies.getObject()).getChildProperties();
					for (OWLProperty<?, ?> op : properties) {
						if (hasIndividuals(op)) {
							return true;
						}
					}

					for (OWLClassExpression subclass : ((OWLClass) object).getSubClasses(ontologies.getObject())) {
						for (OWLClass owlClass : subclass.getClassesInSignature()) {
							if (hasIndividuals(owlClass)) {
								return true;
							}
						}
					}
				}

				return false;
			}
		}
	}
}
