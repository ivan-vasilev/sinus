package bg.bas.iinf.sinus.wicket.application;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.ICompoundRequestMapper;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.resource.SharedResourceReference;

import bg.bas.iinf.sinus.wicket.admin.AnnotationsConfigurationPage;
import bg.bas.iinf.sinus.wicket.admin.AuthRolesPage;
import bg.bas.iinf.sinus.wicket.admin.OntologiesPage;
import bg.bas.iinf.sinus.wicket.admin.OntologyConfigurationPage;
import bg.bas.iinf.sinus.wicket.auth.ConfirmUserPage;
import bg.bas.iinf.sinus.wicket.auth.EditUserPage;
import bg.bas.iinf.sinus.wicket.auth.ForgottenPasswordPage;
import bg.bas.iinf.sinus.wicket.auth.LoginPage;
import bg.bas.iinf.sinus.wicket.auth.RegistrationPage;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.mail.MailEventListener;
import bg.bas.iinf.sinus.wicket.owl.ViewObjectPage;
import bg.bas.iinf.sinus.wicket.owl.annotations.AnnotateObjectPage;
import bg.bas.iinf.sinus.wicket.owl.filter.FilterPage;
import bg.bas.iinf.sinus.wicket.owl.filter.SavedSearchResource;
import bg.bas.iinf.sinus.wicket.owl.filter.SelectedResultsResource;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsPage;

/**
 * glaven Application klas s nastroiki na wicket prilojenieto
 * @author hok
 *
 */
public class SinusApplication extends AuthenticatedWebApplication {

	private MailEventListener mailEventListener;

	public SinusApplication() {
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return FilterPage.class;
	}

	@Override
	protected void init() {
		super.init();

		// tova tuk e za generiraneto na html-a - kogato e true nqma wicket
		// tagove v generiraniq html
		getMarkupSettings().setStripWicketTags(true);
		getDebugSettings().setAjaxDebugModeEnabled(false);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setCompressWhitespace(true);
		getPageSettings().setVersionPagesByDefault(false);

		ICompoundRequestMapper rootRequestMapper = getRootRequestMapperAsCompound();
		rootRequestMapper.add(new MountedMapper("/filter", FilterPage.class));
		rootRequestMapper.add(new MountedMapper("/default_values", OntologyConfigurationPage.class));
		rootRequestMapper.add(new MountedMapper("/login", LoginPage.class));
		rootRequestMapper.add(new MountedMapper("/register", RegistrationPage.class));
		rootRequestMapper.add(new MountedMapper("/confirm_user", ConfirmUserPage.class));
		rootRequestMapper.add(new MountedMapper("/profile", EditUserPage.class));
		rootRequestMapper.add(new MountedMapper("/auth_roles", AuthRolesPage.class));
		rootRequestMapper.add(new MountedMapper("/ontologies", OntologiesPage.class));
		rootRequestMapper.add(new MountedMapper("/annotations", AnnotationsConfigurationPage.class));
		rootRequestMapper.add(new MountedMapper("/search_results", SearchResultsPage.class));
		rootRequestMapper.add(new MountedMapper("/forgotten_password", ForgottenPasswordPage.class));
		rootRequestMapper.add(new MountedMapper("/view_object", ViewObjectPage.class));
		rootRequestMapper.add(new MountedMapper("/annotate_object", AnnotateObjectPage.class));

		getSharedResources().add("saved_searches_ws", new SavedSearchResource());
		mountResource("saved_searches_ws", new SharedResourceReference("saved_searches_ws"));

		getSharedResources().add("selected_results_ws", new SelectedResultsResource());
		mountResource("selected_results_ws", new SharedResourceReference("selected_results_ws"));

		mailEventListener = new MailEventListener();
	}

	/*
	 * (non-Javadoc) syzdava nova sesiq i se opitva da logne potrebitelq ako ima
	 * cookie
	 *
	 * @see
	 * org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.
	 * wicket.request.Request, org.apache.wicket.request.Response)
	 */
	@Override
	public Session newSession(Request request, Response response) {
		UserSession session = new UserSession(request);

		session.setLocale(new Locale("bg", "bg"));
		WebRequest webRequest = (WebRequest) request;
		WebResponse webResponse = (WebResponse) response;

		session.tryAutoLogin(webRequest, webResponse);

		return session;
	}

	@Override
	protected void onDestroy() {
		mailEventListener.interrupt();
	}

	@Override
	public void onEvent(IEvent<?> event) {
		mailEventListener.onEvent(event);
	}

	@Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
	    return UserSession.class;
    }

	@Override
    protected Class<? extends WebPage> getSignInPageClass() {
	    return LoginPage.class;
    }
}
