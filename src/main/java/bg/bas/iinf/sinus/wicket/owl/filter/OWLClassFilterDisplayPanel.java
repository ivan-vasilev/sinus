package bg.bas.iinf.sinus.wicket.owl.filter;

import java.io.Serializable;
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
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import bg.bas.iinf.sinus.cache.ReasonerResolver;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.owl.ChildPropertiesVisitor;
import bg.bas.iinf.sinus.wicket.owl.OWLHierarchicalMenu.ObjectSelectedEP;
import bg.bas.iinf.sinus.wicket.owl.filter.display.OWLDisplayMenu;
import bg.bas.iinf.sinus.wicket.owl.filter.display.OWLViewPropertiesDropDownButton;

/**
 * pokazva menu s property-ta za filtrirane i spisyk s izbranite propertyta + vyzmojnost za zadavane na konkretni stoinosti
 * @author hok
 *
 */
public class OWLClassFilterDisplayPanel extends GenericPanel<OWLClass> {

	private static final long serialVersionUID = -5934569692173886963L;

	private IModel<Set<OWLOntology>> ontologies;
	private DisplayFilterRefreshingView displayView;

	public OWLClassFilterDisplayPanel(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
		super(id, model);
		this.ontologies = ontologies;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setOutputMarkupId(true);

		add(new OWLViewPropertiesDropDownButton("display_menu", getModel(), ontologies) {

            private static final long serialVersionUID = 5847912477409356567L;

			@Override
            @SuppressWarnings("unchecked")
            protected void addMenu(String id) {
				add(new OWLFilterMenu(id, (IModel<OWLClass>) getDefaultModel(), ontologies));
			}
		});

		Form<Void> form = new Form<Void>("display_values_form");
		form.setOutputMarkupId(true);
		add(form);

		WebMarkupContainer wmc = new WebMarkupContainer("display_values_container");
		wmc.setOutputMarkupId(true);
		form.add(wmc);

		wmc.add(displayView = new DisplayFilterRefreshingView("display_values", getModel()));
	}

	/**
	 * spisyk s izbrani za filtyr property-ta
	 * @author hok
	 *
	 */
	public class DisplayFilterRefreshingView extends RefreshingView<OWLNamedObject> {

		private static final long serialVersionUID = -2387118731361524568L;

		private List<IModel<OWLNamedObject>> list = new ArrayList<IModel<OWLNamedObject>>();

        public DisplayFilterRefreshingView(String id, IModel<OWLClass> model) {
			super(id, model);
			list = new ArrayList<IModel<OWLNamedObject>>();
			setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected void populateItem(Item<OWLNamedObject> item) {
			item.setOutputMarkupId(true);

			OWLHierarchyNamedObjectLDM<OWLNamedObject> m = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) item.getModel();
			List<String> names = new LinkedList<String>();
			OWLHierarchyNamedObjectLDM<OWLNamedObject> currentM = m;
			names.add(OWLNamedObjectNameLDM.load(currentM.getObject(), ontologies));
			do {
				if (currentM.getParent() instanceof OWLHierarchyNamedObjectLDM) {
					currentM = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) currentM.getParent();
					if (!(currentM.getObject() instanceof OWLClass)) {
						names.add(OWLNamedObjectNameLDM.load(currentM.getObject(), ontologies));
					}
				} else {
					currentM = null;
				}
			} while (currentM != null);
			Collections.reverse(names);

			item.add(new Label("name", StringUtils.join(names, " -> ")));

			newFilterElement(item);

			item.add(new AjaxLink<OWLNamedObject>("remove", item.getModel()) {

				private static final long serialVersionUID = -2194302015975971565L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					list.remove(getModel());
					Component p = getParent();
					p.remove();
					DojoUtils.removeComponent(target, p);
				}
			});
		}

		@Override
		public Iterator<IModel<OWLNamedObject>> getItemModels() {
			return list.iterator();
		}

        @Override
		public void onEvent(IEvent<?> event) {
			if (event.getPayload() instanceof FilterObjectSelectedEP) {
				FilterObjectSelectedEP payload = (FilterObjectSelectedEP) event.getPayload();
				if (!(payload.getModel().getObject() instanceof OWLObjectProperty)) {
    				Item<OWLNamedObject> item = new Item<OWLNamedObject>(newChildId(), list.size() - 1);
    				item.setModel(payload.getModel());
    				list.add(payload.getModel());
    				add(item);
    				populateItem(item);
    				payload.getTarget().add(item);

    				payload.getTarget().prependJavaScript(
    						String.format(	"var item=document.createElement('%s');" +
    	        							"item.id='%s';" +
    	        							"Wicket.$('%s').appendChild(item);", "tr", item.getMarkupId(true), getParent().getMarkupId(true)));
				}
			}
		}

        /**
         * golqma magiq - tuk na baza na property-to se syzdava daden element ot poterbitelskiq interfeis za filtyr
         * @param item
         */
        @SuppressWarnings("rawtypes")
        private void newFilterElement(Item<OWLNamedObject> item) {
        	OWLHierarchyNamedObjectLDM<? extends OWLNamedObject> owlObjectModel = (OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) item.getModel();
    		OWLNamedObject object = owlObjectModel.getObject();
    		if (object instanceof OWLDataProperty) {
    			OWLDataProperty odp = (OWLDataProperty) object;

    			Set<OWLDataRange> ranges = odp.getRanges(ontologies.getObject());

    			if (ranges.size() == 1) {
    				Set<OWLClassExpression> superClassExpressions = new HashSet<OWLClassExpression>();
    				for (OWLClassExpression domain : odp.getDomains(ontologies.getObject())) {
    					for (OWLClass owlClass : domain.getClassesInSignature()) {
    						superClassExpressions.addAll(owlClass.getSuperClasses(ontologies.getObject()));
    					}
    				}

    				if (owlObjectModel.getParent() != null && owlObjectModel.getParent().getObject() instanceof OWLClass) {
    					OWLClass owlClass = (OWLClass) owlObjectModel.getParent().getObject();
    					superClassExpressions.addAll(owlClass.getSuperClasses(ontologies.getObject()));
    				}

    				for (OWLClassExpression c : superClassExpressions) {
    					if (c instanceof OWLDataSomeValuesFrom) {
    						OWLDataSomeValuesFrom someValues = (OWLDataSomeValuesFrom) c;
    						if (!(someValues.getFiller() instanceof OWLDatatype)) {
    							for (OWLDataProperty prop : someValues.getProperty().getDataPropertiesInSignature()) {
    								if (prop.getIRI().equals(odp.getIRI())) {
    									item.add(new ClassValuesFromLiteralsPanel("edit", new Model<String>(), someValues));
    									return;
    								}
    							}
    						}
    					} else if (c instanceof OWLDataAllValuesFrom) {
    						OWLDataAllValuesFrom allValues = (OWLDataAllValuesFrom) c;
    						if (!(allValues.getFiller() instanceof OWLDatatype)) {
    							for (OWLDataProperty prop : allValues.getProperty().getDataPropertiesInSignature()) {
    								if (prop.getIRI().equals(odp.getIRI())) {
    									item.add(new ClassValuesFromLiteralsPanel("edit", new Model<String>(), allValues));
    									return;
    								}
    							}
    						}
    					} else if (c instanceof OWLDataExactCardinality) {
    						OWLDataExactCardinality car = (OWLDataExactCardinality) c;
    						if (car.getFiller() instanceof OWLDataOneOf) {
    							OWLDataOneOf oneOf = (OWLDataOneOf) car.getFiller();

    							item.add(new OWLLiteralsPanel("edit", new Model<String>(), oneOf.getValues()));
    							return;
    						}
    					}
    				}

    				OWLDatatype datatype = ranges.iterator().next().asOWLDatatype();

    				if (datatype.isDouble()) {
    					item.add(new NumberPanel<Double>("edit", new Model<Serializable>()));
    					return;
    				} else if (datatype.isFloat()) {
    					item.add(new NumberPanel<Float>("edit", new Model<Serializable>()));
    					return;
    				} else if (datatype.isInteger()) {
    					item.add(new NumberPanel<Integer>("edit", new Model<Serializable>()));
    					return;
    				} else if (datatype.isBoolean()) {
    					item.add(new BooleanPanel("edit", new Model<Boolean>()));
    					return;
    				} else if (datatype.getIRI().equals(OWL2Datatype.XSD_DATE_TIME.getIRI())) {
    					item.add(new DatePanel("edit", new Model()));
    					return;
    				}
    			}

    			item.add(new TextFieldPanel("edit", new Model<String>()));
    		} else {
    			item.add(new EmptyPanel("edit"));
    		}
    	}
	}

	public DisplayFilterRefreshingView getDisplayFilterView() {
		return displayView;
	}

	public static class OWLFilterMenu extends OWLDisplayMenu {

		private static final long serialVersionUID = -1071806258148286711L;

		public OWLFilterMenu(String id, IModel<OWLClass> model, IModel<Set<OWLOntology>> ontologies) {
	        super(id, model, ontologies);
        }

		@Override
		protected Set<? extends OWLNamedObject> getRootObjects() {
			OWLClass owlClass = (OWLClass) getDefaultModelObject();
			Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
			result.addAll(new ChildPropertiesVisitor(owlClass, ontologies.getObject()).getChildProperties());
	        for (OWLOntology o : ontologies.getObject()) {
	        	OWLReasoner r = ReasonerResolver.getReasoner(o);
	        	for (OWLClass superClass : r.getSuperClasses(owlClass, true).getFlattened()) {
	        		result.addAll(new ChildPropertiesVisitor(superClass, ontologies.getObject()).getChildProperties());
	        	}

	        	for (OWLClass superClass : r.getSuperClasses(owlClass, false).getFlattened()) {
	        		result.addAll(new ChildPropertiesVisitor(superClass, ontologies.getObject()).getChildProperties());
	        	}
	        }

			return result;
		}

		@Override
		protected Collection<? extends OWLNamedObject> getObjectChildren(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel) {
			OWLNamedObject parent = parentModel.getObject();
			if (parent instanceof OWLClass) {
				OWLClass owlClass = (OWLClass) parent;
				if (owlClass.getIndividuals(ontologies.getObject()).size() > 0) {
					Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
					for (OWLIndividual ind : owlClass.getIndividuals(ontologies.getObject())) {
						result.add(ind.asOWLNamedIndividual());
					}
					return result;
				} else if (owlClass.getSubClasses(ontologies.getObject()).size() > 0) {
					Set<OWLNamedObject> result = new HashSet<OWLNamedObject>();
					for (OWLClassExpression classExp : owlClass.getSubClasses(ontologies.getObject())) {
						result.addAll(classExp.getClassesInSignature());
					}

					result.addAll(new ChildPropertiesVisitor(owlClass, ontologies.getObject()).getChildProperties());

					return result;
				}

				return new ChildPropertiesVisitor(owlClass, ontologies.getObject()).getChildProperties();
			}

			return super.getObjectChildren(parentModel);
		}

		@Override
        protected ObjectSelectedEP createEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
			return new FilterObjectSelectedEP(model, target);
		}
	}

	public IModel<Set<OWLOntology>> getOntologies() {
    	return ontologies;
    }

	private static class FilterObjectSelectedEP extends ObjectSelectedEP {

		public FilterObjectSelectedEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
	        super(model, target);
        }
	}
}
