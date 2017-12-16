package bg.bas.iinf.sinus.hibernate.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.entity.UsersConfirmation;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;

/**
 * listener za Users
 * @author hok
 *
 */
public class UsersListener {

	@PreUpdate
	@PrePersist
	void onPrePersist(Object o) throws IllegalArgumentException {
		if (!(o instanceof Users)) {
			throw new IllegalArgumentException("Argument must be of type Users");
		}

		Users u = (Users) o;

		if (u.getDateCreated() == null) {
			u.setDateCreated(new Date());
		}

		if (StringUtils.isEmpty(u.getAuthRole())) {
			u.setAuthRole(AuthRoles.STUDENT.toString());
		}

		if (u.getUsersConfirmation().size() > 0) {
    		UsersConfirmation uc = u.getUsersConfirmation().iterator().next();
    		if (uc.getUsers() == null) {
    			uc.setUsers(u);
    		}
		}
	}
}
