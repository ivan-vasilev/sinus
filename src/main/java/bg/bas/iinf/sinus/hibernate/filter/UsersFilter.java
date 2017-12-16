package bg.bas.iinf.sinus.hibernate.filter;

import java.io.Serializable;

/**
 * filtyr za potrebiteli
 * @author hok
 *
 */
public class UsersFilter implements Serializable {

	private static final long serialVersionUID = 1500630760401189405L;

	private StringFilter name;
	private PaginationFilter paging;

	public UsersFilter() {
		super();
	}

	public StringFilter getName() {
		return name;
	}

	public void setName(StringFilter name) {
		this.name = name;
	}

	public PaginationFilter getPaging() {
		return paging;
	}

	public void setPaging(PaginationFilter paging) {
		this.paging = paging;
	}
}
