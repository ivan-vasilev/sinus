package bg.bas.iinf.sinus.hibernate.entity;

// Generated 2011-12-28 11:22:15 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import bg.bas.iinf.sinus.hibernate.listener.OntologiesListener;

/**
 * Ontologies generated by hbm2java
 */
@Entity
@Table(name = "ontologies")
@EntityListeners(OntologiesListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ontologies implements java.io.Serializable {

    private static final long serialVersionUID = 2907748685871014977L;

    private int ontologiesId;
	private String id;
	private String uri;
	private boolean isConfigured;
	private Set<DefaultFilterValues> defaultFilterValueses = new HashSet<DefaultFilterValues>(0);

	public Ontologies() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ontologies_id", unique = true, nullable = false)
	public int getOntologiesId() {
		return this.ontologiesId;
	}

	public void setOntologiesId(int ontologiesId) {
		this.ontologiesId = ontologiesId;
	}

	@Column(name = "id", nullable = false, length = 500)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "uri", nullable = false, length = 500)
	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name = "is_configured", nullable = false, length = 500)
	public boolean getIsConfigured() {
		return this.isConfigured;
	}

	public void setIsConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ontologies", orphanRemoval=true, cascade={CascadeType.ALL})
	public Set<DefaultFilterValues> getDefaultFilterValueses() {
		return this.defaultFilterValueses;
	}

	public void setDefaultFilterValueses(Set<DefaultFilterValues> defaultFilterValueses) {
		this.defaultFilterValueses = defaultFilterValueses;
	}
}
