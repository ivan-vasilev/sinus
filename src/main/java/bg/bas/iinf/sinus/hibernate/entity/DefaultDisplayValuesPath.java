package bg.bas.iinf.sinus.hibernate.entity;

// Generated 2011-11-29 23:52:44 by Hibernate Tools 3.4.0.CR1

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
 * DefaultDisplayValuesPath generated by hbm2java
 */
@Entity
@Table(name = "default_display_values_path")
public class DefaultDisplayValuesPath implements java.io.Serializable {

	private static final long serialVersionUID = 3827853722466416165L;

	private Integer defaultDisplayValuesPathsId;
	private DefaultFilterValues defaultFilterValues;
	private String path;

	public DefaultDisplayValuesPath() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "default_display_values_paths_id", unique = true, nullable = false)
	public Integer getDefaultDisplayValuesPathsId() {
		return this.defaultDisplayValuesPathsId;
	}

	public void setDefaultDisplayValuesPathsId(Integer defaultDisplayValuesPathsId) {
		this.defaultDisplayValuesPathsId = defaultDisplayValuesPathsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "default_filter_values_id", nullable = false)
	public DefaultFilterValues getDefaultFilterValues() {
		return this.defaultFilterValues;
	}

	public void setDefaultFilterValues(DefaultFilterValues defaultFilterValues) {
		this.defaultFilterValues = defaultFilterValues;
	}

	@Column(name = "path", nullable = false, length = 16777215)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
