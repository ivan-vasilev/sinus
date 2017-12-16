package bg.bas.iinf.sinus.wicket.owl.filter;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.IResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchResults;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * tova e "web service" chastta - vryshta xml rezultat za zapomneno tyrsene
 * @author hok
 *
 */
public class SavedSearchResource implements IResource {

	private static final Log log = LogFactory.getLog(SavedSearchResource.class);

	public static final String ID_PARAM = "id";

	private static final long serialVersionUID = -691045711708184617L;

	@Override
	public void respond(Attributes attributes) {
		SavedSearches s = Home.findById(ScopedEntityManagerFactory.getEntityManager(), attributes.getParameters().get(ID_PARAM).toOptionalInteger(), SavedSearches.class);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error(e);
			return;
		}

		Document document = docBuilder.newDocument();
		document.appendChild(buildResponseXML(document, s));

		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			WebResponse resp = (WebResponse) attributes.getResponse();
			resp.setContentType("text/xml; charset=UTF-8");
			resp.write(writer.getBuffer().toString());
		} catch (TransformerConfigurationException e) {
			log.error(e);
		} catch (TransformerException e) {
			log.error(e);
		}
	}

	private static Element buildResponseXML(Document doc, SavedSearches search) {
		Element rootElement = doc.createElement("search");

		Element sparqlElement = doc.createElement("sparql");
		sparqlElement.setTextContent(String.valueOf(search.getSparql()));
		rootElement.appendChild(sparqlElement);

		Element sparqlNoContextElement = doc.createElement("sparql_no_context");
		sparqlNoContextElement.setTextContent(String.valueOf(search.getSparqlNoContext()));
		rootElement.appendChild(sparqlNoContextElement);

		Element humanReadableElement = doc.createElement("human_readable");
		humanReadableElement.setTextContent(String.valueOf(search.getHumanReadable()));
		rootElement.appendChild(humanReadableElement);

		Element classElement = doc.createElement("class");
		classElement.setTextContent(String.valueOf(search.getObjectUri()));
		rootElement.appendChild(classElement);

		Element resultsElement = doc.createElement("results");
		rootElement.appendChild(resultsElement);

		Element allSelectedElement = doc.createElement("all_selected");
		allSelectedElement.setTextContent(String.valueOf(search.getAllSelected()));
		resultsElement.appendChild(allSelectedElement);

		Element isSelectedElement = doc.createElement("is_selected");
		isSelectedElement.setTextContent(String.valueOf(search.getIsSelected()));
		resultsElement.appendChild(isSelectedElement);

	    SelectedResultsFilter filter = new SelectedResultsFilter();
	    filter.setUserId(search.getUsers().getUsersId());
	    filter.setClassIRI(search.getObjectUri());

	    for (SearchResults sr : search.getSearchResultses()) {
	    	Element resultElement = doc.createElement("result");
	    	resultElement.setTextContent(String.valueOf(sr.getResult()));
	    	resultsElement.appendChild(resultElement);
		}

		if (search.getSavedSearches() != null) {
			rootElement.appendChild(buildResponseXML(doc, search.getSavedSearches()));
		}

		return rootElement;
	}
}
