package bg.bas.iinf.sinus.wicket.owl;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValueConversionException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.cache.CacheWrapper;
import bg.bas.iinf.sinus.wicket.application.URLConstants;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;

public abstract class OWLHierarchicalMenu extends WebComponent {

    private static final long serialVersionUID = 3444403414822140556L;

	private static final Log log = LogFactory.getLog(OWLHierarchicalMenu.class);

	/**
	 * obekt, koito nqma deca
	 */
	protected static final String NO_CHILDREN_CLICK_MENU_ITEM = "<span dojoType=\"dijit.MenuItem\" onclick=\"wicketAjaxGet('%s&id=%s', function() { }, function() { });\">%s</span>";

	/**
	 * obekt, koito ima deca
	 */
	protected static final String CHILDREN_CLICK_MENU_ITEM = 	"<span dojoType=\"dijit.PopupMenuItem\" onmouseup=\"wicketAjaxGet('%s&id=%s', function() { }, function() { });\">" +
        														"<span>%s</span>" +
        														"<span dojoType=\"dijit.Menu\">"+
        														"%s" +
        														"</span>" +
        														"</span>";

	protected IModel<Set<OWLOntology>> ontologies;
	private int count;
	protected AbstractDefaultAjaxBehavior objectSelector;
	private Map<Integer, OWLHierarchyNamedObjectLDM<OWLNamedObject>> hierarchyModels = new HashMap<Integer, OWLHierarchyNamedObjectLDM<OWLNamedObject>>();

	public OWLHierarchicalMenu(String id, IModel<? extends OWLNamedObject> model, IModel<Set<OWLOntology>> ontologies) {
	    super(id, model);
	    this.ontologies = ontologies;
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setRenderBodyOnly(false);
		setOutputMarkupId(true);

		objectSelector = new AbstractDefaultAjaxBehavior() {

			private static final long serialVersionUID = 5734106819456340781L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters parameters = RequestCycle.get().getRequest().getQueryParameters();
				if (parameters.getParameterNames().contains("id")) {
					try {
						Integer id = parameters.getParameterValue("id").toInteger();
						send(getPage(), Broadcast.BREADTH, createEP(hierarchyModels.get(id), target));
                    } catch (StringValueConversionException e) {
                    	log.error(e);
                    }
				}
			}
		};
		add(objectSelector);

		add(AttributeModifier.replace("dojoType", "dijit.Menu"));
	}

	@SuppressWarnings("unchecked")
    @Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		StringBuilder tagBody = new StringBuilder();
		for (OWLNamedObject rootObject : getRootObjects()) {
			OWLHierarchyNamedObjectLDM<OWLNamedObject> m = new OWLHierarchyNamedObjectLDM<OWLNamedObject>(rootObject, (IModel<? extends OWLNamedObject>) getDefaultModel());
			hierarchyModels.put(++count, m);
			tagBody.append(createTagBody(m, count));
		}

		replaceComponentTagBody(markupStream, openTag, tagBody.toString());
	}

	/**
	 * pravi menu - dyrvo s kategorii
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
    protected String createTagBody(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel, int currentCount) {
		OWLNamedObject object = parentModel.getObject();

		CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
		cache.put(object.getIRI().toURI(), object);

		Collection<? extends OWLNamedObject> children = getObjectChildren(parentModel);
		if (children.size() == 0 ||
			(children.size() == 1 && children.iterator().next().getIRI().equals(OWLRDFVocabulary.OWL_NOTHING.getIRI()))) {
			return String.format(NO_CHILDREN_CLICK_MENU_ITEM, objectSelector.getCallbackUrl(), currentCount, getDisplayValue(object));
		}

		StringBuilder childrenMarkup = new StringBuilder();

		loop:
		for (OWLNamedObject child : children) {
			OWLHierarchyNamedObjectLDM<OWLNamedObject> pm = parentModel;
			while (pm != null) {
				if (pm.getObject().getIRI().equals(child.getIRI())) {
					continue loop;
				}

				if (pm.getParent() != null && pm.getParent() instanceof OWLHierarchyNamedObjectLDM) {
					pm = (OWLHierarchyNamedObjectLDM<OWLNamedObject>) pm.getParent();
				} else {
					pm = null;
				}
			}
			OWLHierarchyNamedObjectLDM<OWLNamedObject> m = new OWLHierarchyNamedObjectLDM<OWLNamedObject>(child, parentModel);
			hierarchyModels.put(++count, m);

			childrenMarkup.append(createTagBody(m, count));
		}

		return String.format(CHILDREN_CLICK_MENU_ITEM, objectSelector.getCallbackUrl(), currentCount, getDisplayValue(object), childrenMarkup.toString());
	}

	private String getDisplayValue(OWLNamedObject object) {
		if (!(object instanceof OWLClass)) {
			return "<i>" + OWLNamedObjectNameLDM.load(object, ontologies) + "</i>";
		}

		return OWLNamedObjectNameLDM.load(object, ontologies);
	}

	protected abstract Collection<? extends OWLNamedObject> getRootObjects();

	protected abstract Collection<? extends OWLNamedObject> getObjectChildren(OWLHierarchyNamedObjectLDM<OWLNamedObject> parentModel);

	protected ObjectSelectedEP createEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
		return new ObjectSelectedEP(model, target);
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScript("dojo.require('dijit.Menu');", URLConstants.REQUIRE_MENU);
		response.renderJavaScript("dojo.require('dijit.PopupMenuItem');", URLConstants.REQUIRE_MENU_ITEM);
	}

	public static class ObjectSelectedEP {

		private OWLHierarchyNamedObjectLDM<OWLNamedObject> model;
		private AjaxRequestTarget target;

		public ObjectSelectedEP(OWLHierarchyNamedObjectLDM<OWLNamedObject> model, AjaxRequestTarget target) {
			super();
			this.model = model;
			this.target = target;
		}

		public OWLHierarchyNamedObjectLDM<OWLNamedObject> getModel() {
			return model;
		}

		public void setModel(OWLHierarchyNamedObjectLDM<OWLNamedObject> model) {
			this.model = model;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public void setTarget(AjaxRequestTarget target) {
			this.target = target;
		}
	}
}
