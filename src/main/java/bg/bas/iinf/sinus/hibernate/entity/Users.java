package bg.bas.iinf.sinus.hibernate.entity;

// Generated 2011-9-24 22:56:57 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import bg.bas.iinf.sinus.hibernate.listener.UsersListener;

/**
 * Users generated by hbm2java
 */
@Entity
@EntityListeners(UsersListener.class)
@Table(name = "users")
public class Users implements java.io.Serializable {

    private static final long serialVersionUID = 4879143970995095766L;

    private Integer usersId;
	private String name;
	private byte[] passwordHash;
	private Date dateCreated;
	private String email;
	private String authRole;
	private Set<UsersConfirmation> usersConfirmation = new HashSet<UsersConfirmation>();
	private Set<SavedSearches> savedSearcheses = new HashSet<SavedSearches>(0);
	private Set<SelectedResults> selectedResultses = new HashSet<SelectedResults>(0);

	public Users() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "users_id", unique = true, nullable = false)
	public Integer getUsersId() {
		return this.usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "password_hash", nullable = false)
	public byte[] getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_created", nullable = false, length = 10)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "auth_role")
	public String getAuthRole() {
		return this.authRole;
	}

	public void setAuthRole(String authRole) {
		this.authRole = authRole;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users", orphanRemoval=true, cascade={CascadeType.ALL})
	public Set<UsersConfirmation> getUsersConfirmation() {
		return this.usersConfirmation;
	}

	public void setUsersConfirmation(Set<UsersConfirmation> usersConfirmation) {
		this.usersConfirmation = usersConfirmation;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<SavedSearches> getSavedSearcheses() {
		return this.savedSearcheses;
	}

	public void setSavedSearcheses(Set<SavedSearches> savedSearcheses) {
		this.savedSearcheses = savedSearcheses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users", orphanRemoval=true, cascade={CascadeType.ALL})
	public Set<SelectedResults> getSelectedResultses() {
		return this.selectedResultses;
	}

	public void setSelectedResultses(Set<SelectedResults> selectedResultses) {
		this.selectedResultses = selectedResultses;
	}
}
