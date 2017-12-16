package bg.bas.iinf.sinus.wicket.owl;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.UrlValidator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bg.bas.iinf.sinus.repository.LiftingUtil;
import bg.bas.iinf.sinus.wicket.model.owl.AllOntologiesLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLNamedObjectNameLDM;
import bg.bas.iinf.sinus.wicket.model.owl.OWLOntologiesLDM;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * Stranica za pylen pregled na obekt (polzva liftinga)
 * @author hok
 *
 */
public class ViewObjectPage extends BasePage {

    private static final long serialVersionUID = 6151811908933120640L;

    public static String PARAMETER_CLASS = "class";
    public static String PARAMETER_URI = "uri";

	public ViewObjectPage(PageParameters parameters) {
	    super(parameters);
	    OWLOntologiesLDM ontologiesLDM = new OWLOntologiesLDM(new HashSet<OWLOntology>(new AllOntologiesLDM(true).getObject()));
	    setDefaultModel(new ViewObjectTableLDM(parameters.get(PARAMETER_CLASS).toOptionalString(), parameters.get(PARAMETER_URI).toOptionalString(), ontologiesLDM));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new WebComponent("content", getDefaultModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
				replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString());
			}
		}.setEscapeModelStrings(false));
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("title", this, null);
	}

	/**
	 * syzdava html tablica sys vsichki property-ta na obekta
	 * @author hok
	 *
	 */
	public static class ViewObjectTableLDM extends LoadableDetachableModel<String> {

        private static final long serialVersionUID = 1L;

		private static final Log log = LogFactory.getLog(ViewObjectTableLDM.class);

		private String classIRI;
		private String objectUri;
		private IModel<Set<OWLOntology>> ontologies;

		public ViewObjectTableLDM(String classIRI, String objectUri, IModel<Set<OWLOntology>> ontologies) {
	        super();
	        this.classIRI = classIRI;
	        this.objectUri = objectUri;
	        this.ontologies = ontologies;
        }

		@Override
		protected void onDetach() {
			super.onDetach();
			ontologies.detach();
		}

		@Override
        protected String load() {
			Set<String> iris = new HashSet<String>();
			iris.add(objectUri);
			String ont = LiftingUtil.getLiftedObjects(iris);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;
			try {
				db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(ont));
				doc = db.parse(is);
			} catch (ParserConfigurationException e) {
				log.error(e);
			} catch (SAXException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}

			OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

			IRI iri = IRI.create(classIRI);

			Set<String> objectNames = new HashSet<String>();
			objectNames.add(iri.getFragment());

			// Now we create the class
			for (OWLClassExpression owlClass : factory.getOWLClass(iri).getSubClasses(ontologies.getObject())) {
				for (OWLClass c : owlClass.getClassesInSignature()) {
					objectNames.add(c.getIRI().getFragment());
				}
			}

			StringBuilder result = new StringBuilder();
			for (String s : objectNames) {
				NodeList individuals = doc.getElementsByTagName(s);
				for (int i = 0; i < individuals.getLength(); i++) {
					createNodeMarkup(doc.getDocumentElement(), individuals.item(i), result);
				}
			}

			return result.toString();
        }

		private void createNodeMarkup(Node mainRoot, Node localRoot, StringBuilder sb) {
			sb.append("<table cellpadding=\"1\" cellspacing=\"0\" border=\"1\">");
			for (int i = 0; i < localRoot.getChildNodes().getLength(); i++) {
				Node child = localRoot.getChildNodes().item(i);

				Node rdfResource = child.getAttributes() != null ? child.getAttributes().getNamedItem("rdf:resource") : null;
				if (rdfResource != null) {
					sb.append("<td>")
					.append(getNodeReadableName(child.getNodeName()))
					.append("</td><td>");

					Node newRoot = LiftingUtil.findByRdfAbout(mainRoot, rdfResource.getTextContent());
					createNodeMarkup(mainRoot, newRoot, sb);
					sb.append("</td></tr>");
				} else {
					String s = child.getTextContent().replaceAll("\n", "");
					if (!StringUtils.isEmpty(s)) {
						sb.append("<tr><td>")
						.append(getNodeReadableName(child.getNodeName()))
						.append("</td><td>");

						UrlValidator urlValidator = new UrlValidator();
    					if (urlValidator.isValid(s)) {
    						if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")) {
    							sb.append("<img src=\"").append(s).append("\" />");
    						} else {
    							sb.append("<a href=\"").append(s).append("\">").append(s).append("</a>");
    						}
    					} else {
    						sb.append(s);
    					}
    					sb.append("</td></tr>");
					}
				}

			}

			sb.append("</table>");
		}

		private String getNodeReadableName(String name) {
			for (OWLOntology ont : ontologies.getObject()) {
				String readableName = OWLNamedObjectNameLDM.load(OWLOntologiesLDM.getOWLNamedObject(IRI.create(ont.getOntologyID().getOntologyIRI() + "#" + name).toURI().toString(), ontologies.getObject()), ontologies);
				if (!StringUtils.isEmpty(readableName)) {
					return readableName;
				}
			}

			return "";
		}
	}
}
