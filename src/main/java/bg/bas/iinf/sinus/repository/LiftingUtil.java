package bg.bas.iinf.sinus.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.semanticweb.owlapi.model.IRI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Util klas za neshtata, svyrzani s liftinga na obekti
 *
 * @author hok
 *
 */
public class LiftingUtil {

	private static final String LIFTING_URL = "http://www.aimsaconference.org/sinus/lifting.php?format=xml";

	public static String getLiftedObjects(Collection<String> iris) {
		if (iris.size() == 0) {
			return null;
		}

		try {
			URL url = new URL(getRequestURL(iris));
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			return IOUtils.toString(in, encoding);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String getRequestURL(Collection<String> iris) {
		if (iris.size() == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(LIFTING_URL);
		for (String id : iris) {
			sb.append("&ids[]=").append(id);
		}

		return sb.toString();
	}

	public static Node findByRdfAbout(Node root, String rdfAbout) {
		NodeList individuals = root.getChildNodes();
		for (int i = 0; i < individuals.getLength(); i++) {
			Node n = individuals.item(i);

			if (n.getAttributes() != null && n.getAttributes().getNamedItem("rdf:about") != null) {
				Node attribute = n.getAttributes().getNamedItem("rdf:about");
				if (attribute != null && attribute.getTextContent().equals(rdfAbout)) {
					return n;
				}

			}
		}

		return null;
	}

	public static String getValue(Node root, Node n, List<String> segments) {
		if (n != null && segments != null && segments.size() > 0) {
			NodeList dataProperties = n.getChildNodes();
			for (int i = 0; i < dataProperties.getLength(); i++) {
				Node p = dataProperties.item(i);
				IRI iri = IRI.create(segments.get(0));
				if (p.getNodeName().equals(iri.getFragment())) {
					if (segments.size() == 1) {
						return p.getTextContent();
					}

					Node rdfResource = p.getAttributes().getNamedItem("rdf:resource");
					if (rdfResource != null) {
						Node newRoot = findByRdfAbout(root, rdfResource.getTextContent());
						return getValue(root, newRoot, segments.subList(2, segments.size()));
					}
				}
			}
		}

		return "";
	}

}
