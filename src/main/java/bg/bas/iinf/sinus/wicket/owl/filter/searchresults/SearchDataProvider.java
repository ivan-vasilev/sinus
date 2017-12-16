package bg.bas.iinf.sinus.wicket.owl.filter.searchresults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLOntology;

import sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.FindIconsQuery;
import sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.FindIconsResult;
import sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.ObjectFactory;
import sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.SinusAdvancedSearchMachineServiceImplementation;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchDisplayValues;
import bg.bas.iinf.sinus.hibernate.entity.SearchResults;

public class SearchDataProvider extends LiftingDataProvider<String> {

	private static final long serialVersionUID = -3627776808638686847L;

	private IModel<SavedSearches> savedSearch;

	public SearchDataProvider(IModel<SavedSearches> savedSearch, IModel<Set<OWLOntology>> ontologies) {
	    super(ontologies);
	    this.savedSearch = savedSearch;
    }

	@Override
	public void detach() {
		super.detach();
		if (savedSearch != null) {
			savedSearch.detach();
		}
	}

	@Override
    public List<String> getIndividualIRIs() {
		if (iris == null) {
    		iris = new ArrayList<String>();
    		if (savedSearch.getObject().getSavedSearchesId() == null) {
    			ObjectFactory factory = new ObjectFactory();
    			FindIconsQuery fiq = factory.createFindIconsQuery();
    			fiq.setSparql(factory.createFindIconsQuerySparql(savedSearch.getObject().getSparql()));
    			FindIconsResult icons = new SinusAdvancedSearchMachineServiceImplementation().getBasicHttp().findIcons(fiq);

    			for (String uri : icons.getURIs().getValue().getString()) {
    				iris.add(uri);
    			}
    		} else {
    			for (SearchResults searchResults : savedSearch.getObject().getSearchResultses()) {
    				iris.add(searchResults.getResult());
    			}
    		}
		}

		return iris;
	}

	@Override
    protected String getRootClassIRI() {
	    return savedSearch.getObject().getObjectUri();
    }

	@Override
    protected List<List<String>> getDisplayPaths() {
		List<List<String>> result = new ArrayList<List<String>>();
		for (SearchDisplayValues sdv : savedSearch.getObject().getSearchDisplayValueses()) {
			result.add(new ArrayList<String>(Arrays.asList(sdv.getUriPath().split(","))));
		}

	    return result;
    }
}
