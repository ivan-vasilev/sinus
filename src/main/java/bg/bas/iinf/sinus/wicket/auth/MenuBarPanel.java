package bg.bas.iinf.sinus.wicket.auth;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * komponent za header-a
 * @author hok
 *
 */
public class MenuBarPanel extends Panel {

    private static final long serialVersionUID = -1784542795124521808L;

	public MenuBarPanel(String id) {
	    super(id);
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setOutputMarkupId(true);

		add(createLoginComponent());
	}

	/**
	 * Syzdava login componenta
	 * @return
	 */
	protected Component createLoginComponent() {
		Component loginComponent = null;

		// spored statusa - kakyv login
		if (UserSession.get().getUserId() != null) {
			loginComponent = new LoggedComponent("login");
		} else {
			loginComponent = new Fragment("login", "login_fragment", this) {

				private static final long serialVersionUID = 601007847928844118L;

				@Override
				protected void onInitialize() {
					super.onInitialize();

					add(new BookmarkablePageLink<RegistrationPage>("create_user", RegistrationPage.class));
					add(new BookmarkablePageLink<LoginPage>("login_link", LoginPage.class));
				}
			};
		}

        return loginComponent;
	}
}
