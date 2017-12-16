package bg.bas.iinf.sinus.wicket.owl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.cache.CacheWrapper;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;

/**
 * menu s OWLNamedObject
 *
 * @author hok
 *
 */
public abstract class OWLMenu extends WebComponent {

    private static final long serialVersionUID = -2948396895907424166L;

	protected static final Log log = LogFactory.getLog(OWLMenu.class);

	/**
	 * kategoriq, koqto nqma deca
	 */
	protected static final String NO_CHILDREN_MENU_ITEM = "<span dojoType=\"dijit.MenuItem\" onclick=\"wicketAjaxGet('%s&uri=%s', function() { }, function() { });\">%s</span>";

	/**
	 * kategoriq, koqto ima deca
	 */
	protected static final String CHILDREN_MENU_ITEM =  "<span dojoType=\"dijit.PopupMenuItem\" onmouseup=\"wicketAjaxGet('%s&uri=%s', function() { }, function() { });\">" +
														"<span>%s</span>" +
														"<span dojoType=\"dijit.Menu\">"+
														"%s" +
														"</span>" +
														"</span>";

	protected IModel<Set<OWLOntology>> ontologies;

	protected AbstractDefaultAjaxBehavior classSelector;

	public OWLMenu(String id, IModel<Set<OWLOntology>> ontologies) {
		super(id);
		this.ontologies = ontologies;
	}

	public OWLMenu(String id, IModel<? extends OWLNamedObject> model, IModel<Set<OWLOntology>> ontologies) {
	    super(id, model);
	    this.ontologies = ontologies;
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setRenderBodyOnly(false);
		setOutputMarkupId(true);

		add(classSelector = createSelector());

		add(AttributeModifier.replace("dojoType", "dijit.Menu"));
	}

	protected AbstractDefaultAjaxBehavior createSelector() {
		return new AbstractDefaultAjaxBehavior() {

			private static final long serialVersionUID = 5734106819456340781L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters parameters = RequestCycle.get().getRequest().getQueryParameters();
				if (parameters.getParameterNames().contains("uri")) {
					try {
	                    URI uri = new URI(parameters.getParameterValue("uri").toString());
	                    onObjectSelected(target, new OWLNamedObjectLDM<OWLNamedObject>(uri));
                    } catch (URISyntaxException e) {
                    	log.error(e);
                    }
				}
			}
		};
	}

	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		StringBuilder tagBody = new StringBuilder();
		for (OWLNamedObject rootObject : getRootObjects()) {
			tagBody.append(createTagBody(rootObject, 1));
		}

		replaceComponentTagBody(markupStream, openTag, tagBody.toString());
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScript("dojo.require('dijit.Menu');", "dijit.Menu");
	}

	/**
	 * pravi menu - dyrvo s kategorii
	 * @param object
	 * @return
	 */
	protected String createTagBody(OWLNamedObject object, int depth) {
		CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
		cache.put(object.getIRI().toURI(), object);

		Set<? extends OWLNamedObject> children = getObjectChildren(object);

		if (children.size() == 0 ||
			depth >= maxDepth() ||
			(children.size() == 1 && children.iterator().next().getIRI().equals(OWLRDFVocabulary.OWL_NOTHING.getIRI()))) {
			String uri = "";
			try {
	            uri = URLEncoder.encode(object.getIRI().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
	            log.error(e);
            }
			return String.format(NO_CHILDREN_MENU_ITEM, classSelector.getCallbackUrl(), uri, getDisplayValue(object));
		}

		StringBuilder childrenMarkup = new StringBuilder();

		for (OWLNamedObject child : children) {
			childrenMarkup.append(createTagBody(child, depth + 1));
		}

		String uri = "";
		try {
			uri = URLEncoder.encode(object.getIRI().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}

		return String.format(CHILDREN_MENU_ITEM, classSelector.getCallbackUrl(), uri, getDisplayValue(object), childrenMarkup.toString());
	}

	private String getDisplayValue(OWLNamedObject object) {
		if (!(object instanceof OWLClass)) {
			return "<i>" + OWLNamedObjectNameLDM.load(object, ontologies) + "</i>";
		}

		return OWLNamedObjectNameLDM.load(object, ontologies);
	}

	/**
	 * maximalna dylbochina, do koqto da se generira dyrvoto (root-a e s dylbochina 0)
	 * @return
	 */
	protected int maxDepth() {
		return Integer.MAX_VALUE;
	};

	protected abstract Set<? extends OWLNamedObject> getRootObjects();

	protected abstract Set<? extends OWLNamedObject> getObjectChildren(OWLNamedObject parent);

	@SuppressWarnings("unused")
    protected void onObjectSelected(AjaxRequestTarget target, IModel<? extends OWLNamedObject> model) {
	};
}
