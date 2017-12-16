package bg.bas.iinf.sinus.hibernate.listener;

import javax.persistence.PrePersist;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import bg.bas.iinf.sinus.hibernate.entity.UsersConfirmation;

/**
 * listener za UsersConfirmation
 * @author hok
 *
 */
public class UsersConfirmationListener {

	private static final int CONFIRM_KEY_LENGTH = 20;

	@PrePersist
	void onPrePersist(Object o) throws IllegalArgumentException {
		if (!(o instanceof UsersConfirmation)) {
			throw new IllegalArgumentException("Argument must be of type UsersConfirmation");
		}

		UsersConfirmation uc = (UsersConfirmation) o;

		if (StringUtils.isEmpty(uc.getConfirmKey())) {
			uc.setConfirmKey(RandomStringUtils.randomAlphanumeric(CONFIRM_KEY_LENGTH));
		}
	}
}
