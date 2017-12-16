package bg.bas.iinf.sinus.wicket.auth;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.UsersConfirmationHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.entity.UsersConfirmation;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.owl.filter.FilterPage;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * stranica za potvyrjdavane na potrebitel
 * @author hok
 *
 */
public class ConfirmUserPage extends BasePage {

	private static final long serialVersionUID = 8710092160357180568L;

	public static final String CONFIRM_KEY_PARAM = "id";

	public ConfirmUserPage(PageParameters parameters) {
		super(parameters);

		String key = parameters.get(CONFIRM_KEY_PARAM).toOptionalString();
		if (!Strings.isEmpty(key)) {
			try {
				EntityManager em = ScopedEntityManagerFactory.getEntityManager();
				UsersConfirmation uc = UsersConfirmationHome.findByConfirmKey(em, key);

				Users u = uc.getUsers();
				Home.persistInOneTransaction(em, u);
				em.detach(u);

				Home.removeInOneTransaction(em, Home.findById(em, uc.getUsersId(), UsersConfirmation.class));

				UserSession us = (UserSession) getSession();
				us.setUserId(u.getUsersId());
			} catch (NoResultException nre) {
				setResponsePage(FilterPage.class);
			}
		} else {
			setResponsePage(FilterPage.class);
		}
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("title", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("title", this, null);
	}

}
