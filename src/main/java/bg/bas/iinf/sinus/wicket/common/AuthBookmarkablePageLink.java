package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import bg.bas.iinf.sinus.wicket.auth.UserSession;

/**
 * tozi link e visible/invisible v zavisimost ot tova dali potrebitelq ima pravo
 * da gleda stranicata, kym koqto linka sochi
 *
 * @author hok
 *
 * @param <T>
 */
public class AuthBookmarkablePageLink<T> extends BookmarkablePageLink<T> {
	private static final long serialVersionUID = 5172286387319481047L;

	public <C extends Page> AuthBookmarkablePageLink(String id, Class<C> pageClass, PageParameters parameters) {
		super(id, pageClass, parameters);
	}

	public <C extends Page> AuthBookmarkablePageLink(String id, Class<C> pageClass) {
		super(id, pageClass);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		AuthorizeInstantiation authInst = getPageClass().getAnnotation(AuthorizeInstantiation.class);
		if (authInst != null) {
			Roles roles = UserSession.get().getRoles();
			for (String v : authInst.value()) {
				if (roles.hasRole(v)) {
					return;
				}
			}

			setVisible(false);
		}
	}
}
