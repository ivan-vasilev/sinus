package bg.bas.iinf.sinus.wicket.search;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import bg.bas.iinf.sinus.hibernate.dao.SavedSearchesHome;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * LDM s imena na zapomnenite sesii (izvlichat se ot zapomnenite tyrseniq)
 * @author hok
 *
 */
public abstract class SavedSearchesListLDM extends LoadableDetachableModel<List<String>> {

    private static final long serialVersionUID = 6300673730548515910L;

	@Override
    protected List<String> load() {
		SavedSearchesFilter f = new SavedSearchesFilter();
		f.setNameFilter(getFilter().getNameFilter());
		f.setObjectUri(getFilter().getObjectUri());
		f.setUserId(getFilter().getUserId());
	    return SavedSearchesHome.getSavedSearchesTags(ScopedEntityManagerFactory.getEntityManager(), f, null);
    }

	public abstract SavedSearchesFilter getFilter();
}
