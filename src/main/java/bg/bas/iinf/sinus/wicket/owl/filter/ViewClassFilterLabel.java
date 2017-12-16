package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedObject;

import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;

/**
 * magiq za pokazvane na informaciq za generirane na human readable text na baza na stoinostite izbrani v OWLClassFilterDisplayPanel
 * @author hok
 *
 */
public abstract class ViewClassFilterLabel extends MultiLineLabel {

    private static final long serialVersionUID = 8782361372214219602L;

	public ViewClassFilterLabel(String id) {
		super(id);
		setDefaultModel(new ViewFilterLDM());
		setEscapeModelStrings(false);
	}

	private class ViewFilterLDM extends LoadableDetachableModel<String> {

        private static final long serialVersionUID = 4042252295091147462L;

        private transient Map<String, Integer> varSequences;
		Map<String, String> paths = new HashMap<String, String>();

        @Override
		protected String load() {
        	if (getFilterPanel() == null) {
        		return "";
        	}

        	StringBuilder sb = new StringBuilder();
        	sb.append((new StringResourceModel("select_all_objects", ViewClassFilterLabel.this, null, new Object[] {"<b>" + OWLNamedObjectNameLDM.load(getFilterPanel().getModelObject(), getFilterPanel().getOntologies()) + "</b>"})).getString());

    		varSequences = new HashMap<String, Integer>();
    		paths = new HashMap<String, String>();
    		Set<String> prefixes = new HashSet<String>();

    		String mainVar = createFilterVar(getFilterPanel().getModelObject(), varSequences);
    		createFilterQueryString(getFilterPanel(), varSequences, paths, prefixes, sb, mainVar);

    		return StringUtils.join(new LinkedHashSet<String>(Arrays.asList(sb.toString().split("\\n"))), "\n");
		}

		@SuppressWarnings("rawtypes")
	    private void createFilterQueryString(OWLClassFilterDisplayPanel panel, Map<String, Integer> varSequences, Map<String, String> paths, Set<String> prefixes, StringBuilder sb, String mainVar) {
			paths.put(((OWLHierarchyNamedObjectLDM) panel.getModel()).toString(), mainVar);

			Iterator<Item<OWLNamedObject>> it = panel.getDisplayFilterView().getItems();
			while (it.hasNext()) {
				Item<OWLNamedObject> item = it.next();
				Component filter = item.get(1);
				if (!(filter instanceof EmptyPanel) && filter instanceof EditableFilterElement) {
					if (!StringUtils.isEmpty(((EditableFilterElement) filter).getTextValue())) {
	    				createFilterQueryString((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) item.getModel(), varSequences, paths, prefixes, sb);
						sb.append(" <i>\"").append(((EditableFilterElement)filter).getTextValue()).append("\"</i>");
					}
				} else {
					createFilterQueryString((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) item.getModel(), varSequences, paths, prefixes, sb);
				}
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
	    private void createFilterQueryString(OWLHierarchyNamedObjectLDM<? extends OWLNamedObject> m, Map<String, Integer> varSequences, Map<String, String> paths, Set<String> prefixes, StringBuilder sb) {
			if (m == null || paths.containsKey(m.toString())) {
				return;
			}

			if (paths.containsKey(m.toString())) {
				return;
			}

			IModel<? extends OWLNamedObject> p = ((OWLHierarchyNamedObjectLDM) m).getParent();
			if (p != null) {
				if (m.getObject() instanceof OWLClass) {
	    			while (p != null && p.getObject() instanceof OWLClass) {
	    				if (p instanceof OWLHierarchyNamedObjectLDM) {
	    					p = ((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) p).getParent();
	    				} else {
	    					p = null;
	    					break;
	    				}
	    			}
				}

    			if (p != null && p instanceof OWLHierarchyNamedObjectLDM) {
    				createFilterQueryString((OWLHierarchyNamedObjectLDM) p, varSequences, paths, prefixes, sb);
    			}
			}

    		IModel<OWLNamedObject> parent = ((OWLHierarchyNamedObjectLDM) m).getParent();
    		if (m.getObject() instanceof OWLClass) {
    			sb.append(" <b>").append(OWLNamedObjectNameLDM.load(m.getObject(), getFilterPanel().getOntologies())).append("</b>");
    		} else {
    			String parentVar = OWLNamedObjectNameLDM.load(parent.getObject(), getFilterPanel().getOntologies());
        		sb.append("\n<b>").append(parentVar).append("</b> ").append(OWLNamedObjectNameLDM.load(m.getObject(), getFilterPanel().getOntologies()));
    		}
		}

		private String createFilterVar(OWLNamedObject o, Map<String, Integer> varSequences) {
			String varName = OWLNamedObjectNameLDM.load(o, getFilterPanel().getOntologies());

			String result = null;
			if (varSequences.containsKey(varName)) {
				result = varName + " " + varSequences.get(varName);
				varSequences.put(varName, varSequences.get(varName) + 1);
			} else {
				result = varName + " 1";
				varSequences.put(varName, 1);
			}

			return result;
		}
	}

	protected abstract OWLClassFilterDisplayPanel getFilterPanel();
}
