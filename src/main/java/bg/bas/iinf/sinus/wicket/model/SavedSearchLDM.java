package bg.bas.iinf.sinus.wicket.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchResults;

/**
 * LDM za SavedSearches
 * @author hok
 *
 */
public class SavedSearchLDM extends AbstractEntityModel<SavedSearches> {

    private static final long serialVersionUID = -3673786983001030885L;

	public SavedSearchLDM() {
	    super();
    }

	public SavedSearchLDM(SavedSearches object) {
	    super(object);
    }

	public SavedSearchLDM(Serializable id) {
	    super(id);
    }

	@Override
    protected Class<SavedSearches> getEntityClass() {
	    return SavedSearches.class;
    }

	@Override
    protected Serializable getId(SavedSearches object) {
	    return object.getSavedSearchesId();
    }

	public Set<String> getSearchResults() {
		if (getObject() == null) {
			return Collections.<String>emptySet();
		}

		Set<String> result = new HashSet<String>();
		for (SearchResults sr : getObject().getSearchResultses()) {
			result.add(sr.getResult());
		}

		return result;
	}

	public IModel<Set<String>> getSearchResultsModel() {
		return new LoadableDetachableModel<Set<String>>() {

			private static final long serialVersionUID = 7950030085350374410L;

			@Override
			protected Set<String> load() {
				return getSearchResults();
			}
		};
	}
}
