package bg.bas.iinf.sinus.wicket.owl.filter.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.owl.ChildPropertiesVisitor;
import bg.bas.iinf.sinus.wicket.owl.OWLHierarchicalMenu.ObjectSelectedEP;

/**
 * pokazva menu s property-ta na klas i spisyk s tekushto izbrani property-ta za pokazvane
 * @author hok
 *
 */
public abstract class OWLClassDisplayPanel extends GenericPanel<OWLClass> {

	private static final long serialVersionUID = -5934569692173886963L;

	protected IModel<Set<OWLOntology>> ontologies;
	private DisplayRefreshingView displayView;

	public OWLClassDisplayPanel(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
		super(id, model);
		this.ontologies = ontologies;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new OWLViewPropertiesDropDownButton("display_menu", getModel(), ontologies) {

			private static final long serialVersionUID = 6086035707256198796L;

            @Override
			protected void addMenu(String id) {
            	add(createMenu(id));
			}
		});

		WebMarkupContainer container = new WebMarkupContainer("display_values_container") {

			private static final long serialVersionUID = -1455335226228925254L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof DisplayObjectSelectedEP) {
					DisplayObjectSelectedEP payload = (DisplayObjectSelectedEP) event.getPayload();
					Component parent = ((Component) event.getSource()).getParent();

					boolean isChild = false;
					while (parent != null && !isChild) {
						isChild = parent.equals(OWLClassDisplayPanel.this);
						parent = parent.getParent();
					}

					if (isChild) {
						payload.getTarget().add(this);
					}
				} else if (event.getPayload() instanceof DisplayEntryRemovedEP) {
					DisplayEntryRemovedEP payload = (DisplayEntryRemovedEP) event.getPayload();
					payload.getTarget().add(this);
				}
			}
		};
		container.setOutputMarkupId(true);
		add(container);

		container.add(displayView = new DisplayRefreshingView("display_values", getModel()));
	}

	protected abstract List<String> getPaths(OWLClass owlClass);

	protected Component createMenu(String id) {
		return new OWLDisplayMenu(id, getModel(), ontologies) {

			private static final long serialVersionUID = 765187116996274974L;

			@Override
			protected ObjectSelectedEP createEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
				return new DisplayObjectSelectedEP(model, target);
			}

			@Override
            protected Collection<? extends OWLNamedObject> getRootObjects() {
				return removeChainProperties(super.getRootObjects());
			}

			@Override
			protected Collection<? extends OWLNamedObject> getObjectChildren(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel) {
				return removeChainProperties(super.getObjectChildren(parentModel));
			}

			private Collection<? extends OWLNamedObject> removeChainProperties(Collection<? extends OWLNamedObject> collection) {
				Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
				skipProperty:
				for (OWLNamedObject o : collection) {
					if (o instanceof OWLObjectProperty) {
						OWLObjectProperty objectProperty = (OWLObjectProperty) o;
						for (OWLOntology ont : ontologies.getObject()) {
							for (OWLAxiom axiom : objectProperty.getReferencingAxioms(ont)) {
								if (axiom instanceof OWLSubPropertyChainOfAxiom) {
									OWLSubPropertyChainOfAxiom chain = (OWLSubPropertyChainOfAxiom) axiom;
									if (!chain.getPropertyChain().contains(objectProperty)) {
										continue skipProperty;
									}
								}
							}
						}
					}

					result.add(o);
				}

				return result;
			}
		};
	}

	@SuppressWarnings("unused")
    protected void propertiesAdded(AjaxRequestTarget target, List<OWLHierarchyNamedObjectLDM<OWLNamedObject>> displayProperties) {
	}

	@SuppressWarnings("unused")
	protected void propertyRemoved(AjaxRequestTarget target, OWLHierarchyNamedObjectLDM<?> model) {
	}

	public DisplayRefreshingView getDisplayView() {
		return displayView;
	}

	/**
	 * spisyk s direktni property-ta na daden obekt
	 * @param parent
	 * @param displayProperties
	 */
	private void getDisplayProperties(OWLHierarchyNamedObjectLDM<OWLNamedObject> parent, List<OWLHierarchyNamedObjectLDM<OWLNamedObject>> displayProperties) {
		if (parent.getObject() instanceof OWLClass) {
			Set<OWLProperty<?, ?>> properties = new ChildPropertiesVisitor((OWLClass) parent.getObject(), ontologies.getObject()).getChildProperties();
			for (OWLProperty<?, ?> p : properties) {
				if (canAddObject(parent, p)) {
					getDisplayProperties(new OWLHierarchyNamedObjectLDM<OWLNamedObject>(p, parent), displayProperties);
				}
			}
		} else if (parent.getObject() instanceof OWLDataProperty) {
			displayProperties.add(parent);
		} else if (parent.getObject() instanceof OWLObjectProperty) {
			OWLObjectProperty objectProperty = (OWLObjectProperty) parent.getObject();
			for (OWLClassExpression rangeEntry : objectProperty.getRanges(ontologies.getObject())) {
				for (OWLClass owlClass : rangeEntry.getClassesInSignature()) {
					if (canAddObject(parent, owlClass)) {
						getDisplayProperties(new OWLHierarchyNamedObjectLDM<OWLNamedObject>(owlClass, parent), displayProperties);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
    private static boolean canAddObject(OWLHierarchyNamedObjectLDM<OWLNamedObject> parent, OWLNamedObject o) {
		OWLHierarchyNamedObjectLDM<OWLNamedObject> currentM = parent;
		do {
			if (currentM.getObject().equals(o)) {
				return false;
			}

			if (currentM.getParent() instanceof OWLHierarchyNamedObjectLDM) {
				currentM = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) currentM.getParent();
			} else {
				currentM = null;
			}
		}
		while (currentM != null);

		return true;
	}

	/**
	 * spisyk s izbrani property-ta za pokazvane - pokazvat se pylnite pytishta
	 * @author hok
	 *
	 */
	public class DisplayRefreshingView extends RefreshingView<OWLNamedObject> {

		private static final long serialVersionUID = -2387118731361524568L;

		private List<IModel<OWLNamedObject>> list = new ArrayList<IModel<OWLNamedObject>>();

		@SuppressWarnings("unchecked")
        public DisplayRefreshingView(String id, IModel<OWLClass> model) {
			super(id, model);

			list = new ArrayList<IModel<OWLNamedObject>>();

			List<String> paths = getPaths(model.getObject());
			if (paths != null && paths.size() > 0) {
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
    				OWLHierarchyNamedObjectLDM<OWLNamedObject> m = new OWLHierarchyNamedObjectLDM<OWLNamedObject>(propertiesVisitor.getChildProperties().iterator().next(), getModel());
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
			}
			while (currentM != null);
			Collections.reverse(names);

			item.add(new Label("name", StringUtils.join(names, " -> ")));
			item.add(new AjaxLink<OWLNamedObject>("remove", item.getModel()) {

				private static final long serialVersionUID = -2194302015975971565L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					list.remove(getModel());
					send(OWLClassDisplayPanel.this, Broadcast.BREADTH, new DisplayEntryRemovedEP(target, (OWLHierarchyNamedObjectLDM<OWLNamedObject>) getModel()));
					propertyRemoved(target, (OWLHierarchyNamedObjectLDM<OWLNamedObject>) getModel());
				}
			});
		}

		@Override
		public Iterator<IModel<OWLNamedObject>> getItemModels() {
			return list.iterator();
		}

        @Override
		public void onEvent(IEvent<?> event) {
			if (event.getPayload() instanceof DisplayObjectSelectedEP) {
				if (event.getSource() instanceof Component) {
					Component parent = ((Component) event.getSource()).getParent();

					boolean isChild = false;
					while (parent != null && !isChild) {
						isChild = parent.equals(OWLClassDisplayPanel.this);
						parent = parent.getParent();
					}

					if (isChild) {
    					DisplayObjectSelectedEP payload = (DisplayObjectSelectedEP) event.getPayload();
    					for (IModel<OWLNamedObject> m : list) {
    						if (m.getObject().getIRI().equals(payload.getModel().getObject().getIRI())) {
    							return;
    						}
    					}

    					List<OWLHierarchyNamedObjectLDM<OWLNamedObject>> displayProperties = new ArrayList<OWLHierarchyNamedObjectLDM<OWLNamedObject>>();
    					getDisplayProperties(payload.getModel(), displayProperties);
    					list.addAll(displayProperties);
    					propertiesAdded(payload.getTarget(), displayProperties);
					}
				}
			}
		}
	}

	public static class DisplayEntryRemovedEP {

		private AjaxRequestTarget target;
		private OWLHierarchyNamedObjectLDM<OWLNamedObject> property;

		public DisplayEntryRemovedEP(AjaxRequestTarget target, OWLHierarchyNamedObjectLDM<OWLNamedObject> property) {
			super();
			this.target = target;
			this.property = property;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public OWLHierarchyNamedObjectLDM<OWLNamedObject> getProperty() {
        	return property;
        }
	}

	public static class DisplayObjectSelectedEP extends ObjectSelectedEP {

		public DisplayObjectSelectedEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
	        super(model, target);
        }
	}
}
