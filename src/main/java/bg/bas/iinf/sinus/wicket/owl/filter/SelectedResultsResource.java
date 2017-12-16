package bg.bas.iinf.sinus.wicket.owl.filter;

import java.io.StringWriter;
import java.util.List;

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

import bg.bas.iinf.sinus.hibernate.dao.SelectedResultsHome;
import bg.bas.iinf.sinus.hibernate.entity.SelectedResults;
import bg.bas.iinf.sinus.hibernate.filter.SelectedResultsFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * otnovo web service - tozi pyt za izbran rezultat
 * @author hok
 *
 */
public class SelectedResultsResource implements IResource {

	private static final Log log = LogFactory.getLog(SelectedResultsResource.class);

	public static final String TAG_PARAM = "id";

	private static final long serialVersionUID = -691045711708184617L;

	@Override
	public void respond(Attributes attributes) {
		if (attributes.getParameters().get(TAG_PARAM).toOptionalString() == null) {
			throw new IllegalArgumentException();
		}

		SelectedResultsFilter filter = new SelectedResultsFilter();
		filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, attributes.getParameters().get(TAG_PARAM).toOptionalString()));

		List<SelectedResults> list = SelectedResultsHome.getSelectedResults(ScopedEntityManagerFactory.getEntityManager(), filter, null);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error(e);
			return;
		}

		Document document = docBuilder.newDocument();
		Element rootElement = document.createElement("results");

		document.appendChild(rootElement);

		for (SelectedResults sr : list) {
			Element resultElement = document.createElement("result");

			Element classElement = document.createElement("class");
			classElement.setTextContent(String.valueOf(sr.getClassIri()));
			resultElement.appendChild(classElement);

			Element objectElement = document.createElement("uri");
			objectElement.setTextContent(String.valueOf(sr.getObjectIri()));
			resultElement.appendChild(objectElement);

			Element savedSearchElement = document.createElement("saved_search");
			savedSearchElement.setTextContent(String.valueOf(sr.getSavedSearches().getSavedSearchesId()));
			resultElement.appendChild(savedSearchElement);

			rootElement.appendChild(resultElement);
		}

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
}
