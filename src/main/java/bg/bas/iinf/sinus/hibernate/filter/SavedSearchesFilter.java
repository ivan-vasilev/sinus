package bg.bas.iinf.sinus.hibernate.filter;

import java.io.Serializable;

/**
 * filtyr za zapomneni tyrseniq
 * @author hok
 *
 */
public class SavedSearchesFilter implements Serializable {

    private static final long serialVersionUID = -3303697157568035939L;

    private StringFilter nameFilter;
	private Integer userId;
	private String objectUri;
	private StringFilter tag;

	public SavedSearchesFilter() {
		super();
	}

	public StringFilter getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(StringFilter nameFilter) {
		this.nameFilter = nameFilter;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getObjectUri() {
    	return objectUri;
    }

	public void setObjectUri(String objectUri) {
    	this.objectUri = objectUri;
    }

	public StringFilter getTag() {
    	return tag;
    }

	public void setTag(StringFilter tag) {
    	this.tag = tag;
    }
}
