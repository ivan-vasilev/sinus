package bg.bas.iinf.sinus.hibernate.filter;

import java.io.Serializable;

import javax.persistence.Query;

/**
 * filtyr za stranicirane
 * @author hok
 *
 */
public class PaginationFilter implements Serializable {

	private static final long serialVersionUID = 3241241596442533599L;

	private Integer startFrom;
	private Integer maxResults;

	public PaginationFilter() {
		super();
	}

	public PaginationFilter(Integer startFrom, Integer maxResults) {
		super();
		this.startFrom = startFrom;
		this.maxResults = maxResults;
	}

	public Integer getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(Integer startFrom) {
		this.startFrom = startFrom;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public void setLimits(Query query) {
		if (query != null) {
			if (getStartFrom() != null) {
				query.setFirstResult(getStartFrom());
			}

			if (getMaxResults() != null) {
				query.setMaxResults(getMaxResults());
			}
		}
	}
}
