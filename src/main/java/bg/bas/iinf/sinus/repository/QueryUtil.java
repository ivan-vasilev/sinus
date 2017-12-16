package bg.bas.iinf.sinus.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;

import bg.bas.iinf.sinus.wicket.model.owl.FromToNumber;
import bg.bas.iinf.sinus.wicket.model.owl.OWLHierarchyNamedObjectLDM;
import bg.bas.iinf.sinus.wicket.owl.filter.BooleanPanel;
import bg.bas.iinf.sinus.wicket.owl.filter.EditableFilterElement;
import bg.bas.iinf.sinus.wicket.owl.filter.LiteralsPanel;
import bg.bas.iinf.sinus.wicket.owl.filter.NumberPanel;
import bg.bas.iinf.sinus.wicket.owl.filter.OWLClassFilterDisplayPanel;
import bg.bas.iinf.sinus.wicket.owl.filter.TextFieldPanel;

/**
 * Util klas za generirane na SPARQL zaqvki
 * @author hok
 *
 */
public class QueryUtil {

	private static final String PREFIXES = 	"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
											"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" +
											"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
											"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";

	public static final String ICONOGRAPHICAL_OBJECT_HAS_URI = "http://www.semanticweb.org/ontologies/2011/1/SINUSBasicOntology.owl#OWLDataProperty_iconographicalObject_has_URI";

	public static String createFullFilterQueryString(OWLClassFilterDisplayPanel panel, Set<String> IRIs) {
		StringBuilder sb = new StringBuilder();

		Map<String, Integer> varSequences = new HashMap<String, Integer>();
		Map<String, String> paths = new HashMap<String, String>();
		Set<String> prefixes = new HashSet<String>();

		String mainVar = createFilterVar(panel.getModelObject(), varSequences);
		sb.append("SELECT ").append(mainVar).append("\n WHERE {\n");
		createFilterQueryString(panel, varSequences, paths, prefixes, sb, mainVar);

		if (IRIs.size() > 0) {
			sb.append(createFilterQuery(mainVar, IRIs, prefixes));
		}

		sb.append("\n}");

		StringBuilder prefixesSB = new StringBuilder();
		prefixesSB.append(PREFIXES);
		for (String prefix : prefixes) {
			prefixesSB.append("PREFIX p").append(String.valueOf(prefix.hashCode())).append(":<").append(prefix).append(">\n");
		}

		return prefixesSB.append(sb.toString()).toString();
	}

	public static String createFullFilterQueryString(OWLClassFilterDisplayPanel panel) {
		StringBuilder sb = new StringBuilder();

		Map<String, Integer> varSequences = new HashMap<String, Integer>();
		Map<String, String> paths = new HashMap<String, String>();
		Set<String> prefixes = new HashSet<String>();

		String mainVar = createFilterVar(panel.getModelObject(), varSequences);
		sb.append("SELECT ").append(mainVar).append("\n WHERE {\n");
		createFilterQueryString(panel, varSequences, paths, prefixes, sb, mainVar);
		sb.append("\n}");

		StringBuilder prefixesSB = new StringBuilder();
		prefixesSB.append(PREFIXES);
		for (String prefix : prefixes) {
			prefixesSB.append("PREFIX p").append(String.valueOf(prefix.hashCode())).append(":<").append(prefix).append(">\n");
		}

		return getPrefixes(prefixes) + sb.toString();
	}

	/**
	 * vryshta stoinostta ot hranilishteto
	 * @param chain - veriga ot property-ta
	 * @param uri - uri na obekta
	 * @return
	 */
	public static String createSelectValueQuery(List<OWLNamedObject> chain, String uri) {
		String mainVar = "?v";
		Set<String> prefixes = new HashSet<String>();

		List<String> chainStatements = new ArrayList<String>();
		int varCount = 1;
		for (OWLNamedObject o : chain) {
			if (o instanceof OWLClass) {
				chainStatements.add(mainVar + varCount + " rdf:type " + getPrefixedName(o.getIRI(), prefixes));
			} else {
				chainStatements.add(mainVar + varCount + " " + getPrefixedName(o.getIRI(), prefixes) + " " + mainVar + (++varCount));
			}
		}

		StringBuilder sb = new StringBuilder();
		sb	.append("SELECT ").append(mainVar + varCount)
			.append("\n WHERE {\n")
			.append(StringUtils.join(chainStatements, " .\n"))
			.append(" .\n").append(mainVar + "1").append(" ").append(getPrefixedName(IRI.create(ICONOGRAPHICAL_OBJECT_HAS_URI), prefixes)).append(" ?uri")
			.append(" .\nFILTER(str(?uri) IN (\"" + uri + "\"))")
			.append("\n}");

		return getPrefixes(prefixes) + sb.toString();
	}

	/**
	 * update na stoinost v hranilishteto
	 * @param chain - veriga ot property-ta
	 * @param uri - uri na obekta
	 * @param newValue - nova stoinost
	 * @return
	 */
	public static String createUpdateValueQuery(List<OWLNamedObject> chain, String uri, String newValue) {
		String mainVar = "?v";
		Set<String> prefixes = new HashSet<String>();

		OWLNamedObject propertyToUpdate = chain.remove(chain.size() - 1);
		List<String> chainStatements = new ArrayList<String>();
		int varCount = 1;
		for (OWLNamedObject o : chain) {
			if (o instanceof OWLClass) {
				chainStatements.add(mainVar + varCount + " rdf:type " + getPrefixedName(o.getIRI(), prefixes));
			} else {
				chainStatements.add(mainVar + varCount + " " + getPrefixedName(o.getIRI(), prefixes) + " " + mainVar + (++varCount));
			}
		}

		StringBuilder sb = new StringBuilder();
		sb	.append("DELETE {").append(mainVar + varCount).append(" ").append(getPrefixedName(propertyToUpdate.getIRI(), prefixes)).append(" ?allMatches }")
			.append("\nINSERT {").append(mainVar + varCount).append(" ").append(getPrefixedName(propertyToUpdate.getIRI(), prefixes)).append(" ").append(newValue).append(" }")
			.append("\n WHERE {\n")
			.append(StringUtils.join(chainStatements, " .\n"))
			.append(" .\n").append(mainVar + "1").append(" ").append(getPrefixedName(IRI.create(ICONOGRAPHICAL_OBJECT_HAS_URI), prefixes)).append(" ?uri")
			.append(" .\nFILTER(str(?uri) IN (\"" + uri + "\"))")
			.append("\n}");

		return getPrefixes(prefixes) + sb.toString();
	}

	private static String getPrefixes(Set<String> prefixes) {
		StringBuilder prefixesSB = new StringBuilder();
		prefixesSB.append(PREFIXES);
		for (String prefix : prefixes) {
			prefixesSB.append("PREFIX p").append(String.valueOf(prefix.hashCode())).append(":<").append(prefix).append(">\n");
		}

		return prefixesSB.toString();
	}

	private static String createFilterQuery(String varName, Set<String> IRIs, Set<String> prefixes) {
		StringBuilder result = new StringBuilder();
		if (IRIs.size() > 0) {
			IRI iri = IRI.create(ICONOGRAPHICAL_OBJECT_HAS_URI);
			result.append(" .\n").append(varName).append(" ").append(getPrefixedName(iri, prefixes)).append(" ?uri .\n");
			result.append(" FILTER(str(?uri) IN (");
			Iterator<String> it = IRIs.iterator();
			result.append("\"").append(it.next()).append("\"");
			while (it.hasNext()) {
				result.append(",\"").append(it.next()).append("\"");
			}
			result.append("))");
		}

		return result.toString();
	}

	@SuppressWarnings("rawtypes")
    private static void createFilterQueryString(OWLClassFilterDisplayPanel panel, Map<String, Integer> varSequences, Map<String, String> pathsVars, Set<String> prefixes, StringBuilder sb, String mainVar) {
		sb.append(mainVar).append(" rdf:type ").append(getPrefixedName(panel.getModelObject().getIRI(), prefixes));
		pathsVars.put(((OWLHierarchyNamedObjectLDM) panel.getModel()).toString(), mainVar);

		Iterator<Item<OWLNamedObject>> it = panel.getDisplayFilterView().getItems();
		while (it.hasNext()) {
			Item<OWLNamedObject> item = it.next();
			Component filter = item.get(1);
			if (!(filter instanceof EmptyPanel) && filter instanceof EditableFilterElement) {
				if (!StringUtils.isEmpty(((EditableFilterElement) filter).getTextValue())) {
    				createFilterQueryString((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) item.getModel(), varSequences, pathsVars, prefixes, sb);
    				if ((filter instanceof TextFieldPanel || filter instanceof LiteralsPanel) && filter.getDefaultModelObject() != null) {
    					String varName = getVarName(item.getModel(), pathsVars, varSequences);
    					sb.append(" .\nFILTER regex(").append(varName).append(", \"").append(((EditableFilterElement) filter).getTextValue());
    					sb.append("\", \"i\")");
    				} else if (filter instanceof NumberPanel) {
    					if (filter.getDefaultModelObject() instanceof FromToNumber) {
    						FromToNumber<?> fromToNumber = (FromToNumber<?>) filter.getDefaultModelObject();
    						if (fromToNumber.getFrom() != null || fromToNumber.getTo() != null) {
    	    					String varName = getVarName(item.getModel(), pathsVars, varSequences);
    							if (fromToNumber.getFrom() != null) {
    								sb.append(" .\nFILTER (").append(varName).append(" >= ").append(fromToNumber.getFrom()).append(")");
    							}

    							if (fromToNumber.getTo() != null) {
    								sb.append(" .\nFILTER (").append(varName).append(" <= ").append(fromToNumber.getTo()).append(")");
    							}
    						}
    					} else if (filter.getDefaultModelObject() != null) {
        					String varName = getVarName(item.getModel(), pathsVars, varSequences);
    						sb.append(" .\nFILTER (").append(varName).append(" = ").append(filter.getDefaultModelObjectAsString()).append(")");
    					}
    				} else if (filter instanceof BooleanPanel && filter.getDefaultModelObject() != null) {
    					sb.append(" .\nFILTER (").append(filter.getDefaultModelObjectAsString()).append(")");
    				}
				}
			} else {
				createFilterQueryString((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) item.getModel(), varSequences, pathsVars, prefixes, sb);
			}
		}
	}

    @SuppressWarnings("unchecked")
    private static void createFilterQueryString(OWLHierarchyNamedObjectLDM<? extends OWLNamedObject> m, Map<String, Integer> varSequences, Map<String, String> pathsVars, Set<String> prefixes, StringBuilder sb) {
		if (m == null || pathsVars.containsKey(m.toString())) {
			return;
		}

		IModel<? extends OWLNamedObject> p = m.getParent();
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
				createFilterQueryString((OWLHierarchyNamedObjectLDM<OWLNamedObject>) p, varSequences, pathsVars, prefixes, sb);
			}
		}

		IModel<OWLNamedObject> parent = (IModel<OWLNamedObject>) m.getParent();
		String parentVar = getVarName(parent, pathsVars, varSequences);
		if (m.getObject() instanceof OWLClass) {
			if (!pathsVars.containsKey(parent.toString())) {
				String queryElement = " .\n" + parentVar + " rdf:type " + getPrefixedName(m.getObject().getIRI(), prefixes);
				addQueryElement(sb, queryElement);
			}
		} else if (m.getObject() instanceof OWLIndividual) {
			String queryElement = " .\nFILTER (" + parentVar + " = " + getPrefixedName(m.getObject().getIRI(), prefixes) + ")";
			addQueryElement(sb, queryElement);
		} else {
			String queryElement = " .\n" + parentVar + " " + getPrefixedName(m.getObject().getIRI(), prefixes) + " " + getVarName(m, pathsVars, varSequences);
			addQueryElement(sb, queryElement);
		}
	}

    private static void addQueryElement(StringBuilder query, String element) {
    	if (!query.toString().contains(element)) {
    		query.append(element);
    	}
    }

    @SuppressWarnings("rawtypes")
    private static String getVarName(IModel<? extends OWLNamedObject> m, Map<String, String> paths, Map<String, Integer> varSequences) {
		if (m instanceof OWLHierarchyNamedObjectLDM && paths.containsKey(m.toString())) {
			return paths.get(m.toString());
		}

		if (paths.containsKey(m.getObject().getIRI())) {
			return paths.get(m.getObject().getIRI());
		}

		if (m.getObject() instanceof OWLClass) {
    		if (m instanceof OWLHierarchyNamedObjectLDM) {
    			IModel<? extends OWLNamedObject> p = m;
    			while (p != null && p.getObject() instanceof OWLClass) {
    				if (p instanceof OWLHierarchyNamedObjectLDM) {
    					p = ((OWLHierarchyNamedObjectLDM<? extends OWLNamedObject>) p).getParent();
    				} else {
    					p = null;
    				}
    			}

    			if (p != null) {
    				m = p;

    				if (m instanceof OWLHierarchyNamedObjectLDM && paths.containsKey(m.toString())) {
    					return paths.get(m.toString());
    				}

    				if (paths.containsKey(m.getObject().getIRI())) {
    					return paths.get(m.getObject().getIRI());
    				}
    			}
    		}
		}

		String var = createFilterVar(m.getObject(), varSequences);
		if (m instanceof OWLHierarchyNamedObjectLDM) {
			paths.put(((OWLHierarchyNamedObjectLDM) m).toString(), var);
		} else {
			paths.put(m.getObject().getIRI().toString(), var);
		}
		return var;
	}

	private static String getPrefixedName(IRI iri, Set<String> prefixes) {
		if (!prefixes.contains(iri.getStart())) {
			prefixes.add(iri.getStart());
		}

		return "p" + iri.getStart().hashCode() + ":" + iri.getFragment();
	}

	private static String createFilterVar(OWLNamedObject o, Map<String, Integer> varSequences) {
		String varName = o.getIRI().getFragment();

		String result = null;
		if (varSequences.containsKey(varName)) {
			result = varName + "_" + varSequences.get(varName);
			varSequences.put(varName, varSequences.get(varName) + 1);
		} else {
			result = varName + "_1";
			varSequences.put(varName, 2);
		}

		return "?" + result;
	}
}
