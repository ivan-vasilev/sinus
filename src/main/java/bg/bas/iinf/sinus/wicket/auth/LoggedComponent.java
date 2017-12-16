package bg.bas.iinf.sinus.wicket.auth;

import javax.servlet.http.Cookie;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.wicket.admin.AnnotationsConfigurationPage;
import bg.bas.iinf.sinus.wicket.admin.AuthRolesPage;
import bg.bas.iinf.sinus.wicket.admin.OntologiesPage;
import bg.bas.iinf.sinus.wicket.application.URLConstants;
import bg.bas.iinf.sinus.wicket.owl.filter.FilterPage;

/**
 * komponent, koito sedi v header-a, kogato potrebitelq e lognat
 * @author hok
 *
 */
public class LoggedComponent extends Panel {

	private static final long serialVersionUID = 2524353706590112364L;

	public LoggedComponent(String id) {
		super(id);

		Users user = UserSession.get().getUser();

		add(new AdminDropDownButton("admin"));

		BookmarkablePageLink<FilterPage> search = new BookmarkablePageLink<FilterPage>("search", FilterPage.class);
		add(search);
		MetaDataRoleAuthorizationStrategy.authorize(search, RENDER, AuthRoles.STUDENT.toString());

		add(new BookmarkablePageLink<EditUserPage>("edit_user", EditUserPage.class).setBody(new Model<String>(user.getName())));

		add(new Link<Page>("logout") {

			private static final long serialVersionUID = 6762033052623200948L;

			@Override
			public void onClick() {
				((UserSession) getSession()).signOut();
				Cookie pass = ((WebRequest)getRequest()).getCookie(UserSession.COOKIE_PASSWORD);
				if (pass != null) {
					((WebResponse)getResponse()).clearCookie(pass);
				}
				setResponsePage(getApplication().getHomePage());
			}
		});
	}

	public class AdminDropDownButton extends WebMarkupContainer {

	    public AdminDropDownButton(String id) {
	        super(id);
        }

		private static final long serialVersionUID = 7251832587752120083L;


	    @Override
		protected void onInitialize() {
			super.onInitialize();

			WebMarkupContainer authRoles = new WebMarkupContainer("auth_roles");
			authRoles.add(AttributeModifier.replace("onclick", new LoadableDetachableModel<String>() {

				private static final long serialVersionUID = -708548833862551751L;

				@Override
				protected String load() {
					return String.format("window.location.href=\"%s\"", urlFor(AuthRolesPage.class, null));
				}
			}));
			add(authRoles);
			MetaDataRoleAuthorizationStrategy.authorize(authRoles, RENDER, AuthRoles.ADMIN.toString());

			WebMarkupContainer ontologies = new WebMarkupContainer("ontologies");
			ontologies.add(AttributeModifier.replace("onclick", new LoadableDetachableModel<String>() {

				private static final long serialVersionUID = -708548833862551751L;

				@Override
				protected String load() {
					return String.format("window.location.href=\"%s\"", urlFor(OntologiesPage.class, null));
				}
			}));
			add(ontologies);
			MetaDataRoleAuthorizationStrategy.authorize(ontologies, RENDER, AuthRoles.LIBRARIAN.toString());

			WebMarkupContainer annotationsConfiguration = new WebMarkupContainer("annotations_configuration");
			annotationsConfiguration.add(AttributeModifier.replace("onclick", new LoadableDetachableModel<String>() {

				private static final long serialVersionUID = -708548833862551751L;

				@Override
				protected String load() {
					return String.format("window.location.href=\"%s\"", urlFor(AnnotationsConfigurationPage.class, null));
				}
			}));
			add(annotationsConfiguration);
			MetaDataRoleAuthorizationStrategy.authorize(annotationsConfiguration, RENDER, AuthRoles.LIBRARIAN.toString());
		}

		@Override
		public void renderHead(final IHeaderResponse response) {
			response.renderJavaScript("dojo.require('dijit.form.DropDownButton');", URLConstants.REQUIRE_DROP_DOWN_BUTTON);
			response.renderJavaScript("dojo.require('dijit.Menu');", URLConstants.REQUIRE_MENU);
			response.renderJavaScript("dojo.require('dijit.PopupMenuItem');", URLConstants.REQUIRE_MENU_ITEM);
		}

		@Override
		protected void onConfigure() {
			super.onConfigure();
			MetaDataRoleAuthorizationStrategy.authorize(this, RENDER,  AuthRoles.ADMIN.toString() + "," + AuthRoles.LIBRARIAN.toString());
		}
	}
}
