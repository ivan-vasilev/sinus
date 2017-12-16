package bg.bas.iinf.sinus.hibernate.entity;

// Generated 2011-11-2 12:09:22 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SearchDisplayValues generated by hbm2java
 */
@Entity
@Table(name = "search_display_values")
public class SearchDisplayValues implements java.io.Serializable {

    private static final long serialVersionUID = -2572674143853501925L;

    private Integer searchDisplayValuesId;
	private SavedSearches savedSearches;
	private String uriPath;

	public SearchDisplayValues() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "search_display_values_id", unique = true, nullable = false)
	public Integer getSearchDisplayValuesId() {
		return this.searchDisplayValuesId;
	}

	public void setSearchDisplayValuesId(Integer searchDisplayValuesId) {
		this.searchDisplayValuesId = searchDisplayValuesId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "saved_searches_id", nullable = false)
	public SavedSearches getSavedSearches() {
		return this.savedSearches;
	}

	public void setSavedSearches(SavedSearches savedSearches) {
		this.savedSearches = savedSearches;
	}

	@Column(name = "uri_path", nullable = false, length = 10000)
	public String getUriPath() {
		return this.uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

}