package bg.bas.iinf.sinus.wicket.search;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import bg.bas.iinf.sinus.hibernate.dao.SavedSearchesHome;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.filter.PaginationFilter;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.model.SavedSearchLDM;

/**
 * DataProvider za zapametenite tyrseniq na daden potrebitel
 * @author hok
 *
 */
public class SavedSearchesDataProvider implements IDataProvider<SavedSearches> {

    private static final long serialVersionUID = 5683422969199019419L;

    private SavedSearchesFilter filter;

    protected transient Integer size;

    public SavedSearchesDataProvider(SavedSearchesFilter filter) {
	    super();
	    this.filter = filter;
    }

	@Override
    public void detach() {
		size = null;
    }

	@Override
    public Iterator<? extends SavedSearches> iterator(int first, int count) {
		PaginationFilter pf = new PaginationFilter(first, count);
		return SavedSearchesHome.getSavedSearches(ScopedEntityManagerFactory.getEntityManager(), filter, pf).iterator();
    }

	@Override
    public int size() {
		if (size == null) {
			size = SavedSearchesHome.getSavedSearchesCount(ScopedEntityManagerFactory.getEntityManager(), filter).intValue();
		}

		return size;
	}

	@Override
	public IModel<SavedSearches> model(SavedSearches object) {
		return new SavedSearchLDM(object);
	}
}
