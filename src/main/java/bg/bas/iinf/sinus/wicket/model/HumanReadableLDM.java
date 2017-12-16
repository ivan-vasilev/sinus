package bg.bas.iinf.sinus.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;

import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;

/**
 * model, koito pokazva "Human readable" chastta na zapomneno tyrsene
 * @author hok
 *
 */
public class HumanReadableLDM extends LoadableDetachableModel<String> {

    private static final long serialVersionUID = 5711815437311700967L;

    private IModel<SavedSearches> search;

	public HumanReadableLDM(IModel<SavedSearches> search) {
	    super();
	    this.search = search;
    }

	@Override
    protected String load() {
	    if (search.getObject().getSavedSearches() != null) {
	    	return search.getObject().getHumanReadable() + "\n&nbsp;&nbsp;&nbsp;&nbsp;<b>" + new ResourceModel("search_within_context").getObject() + "</b>\n" + search.getObject().getSavedSearches().getHumanReadable();
	    }

	    return search.getObject().getHumanReadable();
    }

}
