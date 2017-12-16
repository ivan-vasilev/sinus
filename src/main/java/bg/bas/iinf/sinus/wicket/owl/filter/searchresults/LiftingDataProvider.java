package bg.bas.iinf.sinus.wicket.owl.filter.searchresults;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
import bg.bas.iinf.sinus.repository.QueryUtil;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView.OWLNamedObjectResult;

/**
 * tozi DataProvider "lift-va" obekti na baza spisyk s "iri"
 * @author hok
 *
 * @param <T>
 */
public abstract class LiftingDataProvider<T> implements IDataProvider<OWLNamedObjectResult<T>> {

	private static final Log log = LogFactory.getLog(SearchDataProvider.class);

	private static final long serialVersionUID = -3627776808638686847L;

	protected IModel<Set<OWLOntology>> ontologies;
	protected transient List<OWLNamedObjectResult<T>> result;
	protected transient List<String> iris;

	public LiftingDataProvider(IModel<Set<OWLOntology>> ontologies) {
	    super();
	    this.ontologies = ontologies;
    }

	@Override
	public void detach() {
		result = null;
		iris = null;

		if (ontologies != null) {
			ontologies.detach();
		}
	}

	@Override
	public Iterator<? extends OWLNamedObjectResult<T>> iterator(int first, int count) {
		if (result == null) {
			result = new ArrayList<OWLNamedObjectResult<T>>();
			if (iris == null) {
				iris = getIndividualIRIs();
			}

			String ont = LiftingUtil.getLiftedObjects(iris.subList(first, first + count));

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

			IRI iri = IRI.create(getRootClassIRI());
			Set<String> objectNames = new HashSet<String>();
			objectNames.add(iri.getFragment());

			// Now we create the class
			for (OWLClassExpression owlClass : factory.getOWLClass(iri).getSubClasses(ontologies.getObject())) {
				for (OWLClass c : owlClass.getClassesInSignature()) {
					objectNames.add(c.getIRI().getFragment());
				}
			}

			for (String s : objectNames) {
				NodeList individuals = doc.getElementsByTagName(s);
				for (int i = 0; i < individuals.getLength(); i++) {
					Node n = individuals.item(i);
					List<String> path = new ArrayList<String>();
					path.add(IRI.create(QueryUtil.ICONOGRAPHICAL_OBJECT_HAS_URI).toURI().toString());
					String uri = LiftingUtil.getValue(doc.getDocumentElement(), n, path);
                    OWLNamedObjectResult<T> oor = new OWLNamedObjectResult<T>(getTForUri(uri));
					for (List<String> p : getDisplayPaths()) {
						oor.getProperties().add(LiftingUtil.getValue(doc.getDocumentElement(), n, p));
					}

					result.add(oor);
				}
			}
		}

		return result.iterator();
	}

	@Override
	public int size() {
		if (iris == null) {
			iris = getIndividualIRIs();
		}

		return iris.size();
	}

	@Override
	public IModel<OWLNamedObjectResult<T>> model(OWLNamedObjectResult<T> object) {
		return new Model<OWLNamedObjectResult<T>>(object);
	}

	public abstract List<String> getIndividualIRIs();
	protected abstract String getRootClassIRI();
	protected abstract List<List<String>> getDisplayPaths();

	@SuppressWarnings("unchecked")
    protected T getTForUri(String uri) {
		return (T) uri;
	};
}
