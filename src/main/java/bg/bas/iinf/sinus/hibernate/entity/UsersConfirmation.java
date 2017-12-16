package bg.bas.iinf.sinus.hibernate.entity;

// Generated 2011-9-24 22:56:57 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import bg.bas.iinf.sinus.hibernate.listener.UsersConfirmationListener;

/**
 * UsersConfirmation generated by hbm2java
 */
@Entity
@EntityListeners(UsersConfirmationListener.class)
@Table(name = "users_confirmation")
public class UsersConfirmation implements java.io.Serializable {

    private static final long serialVersionUID = 1216053708729970733L;

    private int usersId;
	private Users users;
	private String confirmKey;

	public UsersConfirmation() {
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "users"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "users_id", unique = true, nullable = false)
	public int getUsersId() {
		return this.usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", insertable=false, updatable=false)
	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "confirm_key", nullable = false)
	public String getConfirmKey() {
		return this.confirmKey;
	}

	public void setConfirmKey(String confirmKey) {
		this.confirmKey = confirmKey;
	}

}
