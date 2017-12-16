package bg.bas.iinf.sinus.hibernate.listener;

import java.util.Date;

import javax.persistence.PrePersist;

import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.entity.SearchDisplayValues;
import bg.bas.iinf.sinus.wicket.auth.UserSession;

/**
 * listener za SavedSearches
 * @author hok
 *
 */
public class SavedSearchesListener {

	@PrePersist
	void onPrePersist(Object o) throws IllegalArgumentException {
		if (!(o instanceof SavedSearches)) {
			throw new IllegalArgumentException("Argument must be of type SavedSearches");
		}

		SavedSearches u = (SavedSearches) o;

		if (u.getDateCreated() == null) {
			u.setDateCreated(new Date());
		}

		if (u.getSavedSearchesId() == null) {
			for (SearchDisplayValues sdv : u.getSearchDisplayValueses()) {
				sdv.setSavedSearches(u);
			}
		}

		if (u.getUsers() == null) {
			u.setUsers(UserSession.get().getUser());
		}
	}
}
