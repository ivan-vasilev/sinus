package bg.bas.iinf.sinus.wicket.auth;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.StringValidator;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.wicket.application.URLConstants;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.common.Util;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * Stranica za potrebitelski vhod
 * @author hok
 *
 */
public class LoginPage extends BasePage {

    private static final long serialVersionUID = 8423695813927887766L;

    private Label loginLabel;

    public LoginPage() {
		super(new ResourceModel("user_enter_label"));
		checkLoginStatus();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		loginLabel = new Label("login_message", new Model<String>(""));
		add(loginLabel);

		if (getDefaultModelObject() != null) {
			loginLabel.setDefaultModel(getDefaultModel());
		}

		add(new LoginForm("login_form") {

			private static final long serialVersionUID = -640292150786824451L;

			@Override
			public void renderHead(final IHeaderResponse response) {
				super.renderHead(response);

				// focus na username poleto
				response.renderOnLoadJavaScript(String.format("document.getElementById('%s').focus()", usernameField.getMarkupId(true)));
				response.renderOnDomReadyJavaScript("initCheckBoxes('" + getMarkupId() + "');");
			}

			@Override
			protected void onSubmit() {
				if (!continueToOriginalDestination()) {
					setResponsePage(Application.get().getHomePage());
				}
			}

			@Override
			protected void onLoginFailed() {
				loginLabel.setDefaultModel(new StringResourceModel("not_found_message", this, null));
				loginLabel.add(AttributeModifier.append("style", "color:#d00;"));
			}
		});

		add(new BookmarkablePageLink<RegistrationPage>("create_user", RegistrationPage.class));
		add(new BookmarkablePageLink<ForgottenPasswordPage>("forgotten_password", ForgottenPasswordPage.class));
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("enter_label", this, null);
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("enter_label", this, null);
	}

	private void checkLoginStatus() throws RestartResponseException {
		UserSession session = (UserSession) getSession();
		if (session.getUserId() != null) {
			throw new RestartResponseException(Application.get().getHomePage());
		}
	}

	@Override
    public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavaScript("dojo.require('dijit.Tooltip');", URLConstants.REQUIRE_TOOLTIP);
	}

	private static abstract class LoginForm extends ErrorHighlightingForm<Users> {

		private static final long serialVersionUID = 867492200791785600L;

		private static final Log log = LogFactory.getLog(LoginForm.class);
		private static final String REMEMBER_ME_ID = "rememberMe";

		protected RequiredTextField<String> usernameField;
		protected PasswordTextField passwordField;
		protected CheckBox rememberMeCkb;
		protected Button loginButton;

		public LoginForm(String id) {
			super(id);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			usernameField = new RequiredTextField<String>("username", new Model<String>(""));
			usernameField.setOutputMarkupId(true);
			usernameField.setRequired(true);
			add(usernameField);

			passwordField = new PasswordTextField("password", new Model<String>(""));
			passwordField.add(StringValidator.maximumLength(255));
			passwordField.setResetPassword(false);
			add(passwordField);

			loginButton = createLoginButton("login_button");
			add(loginButton);

			Cookie rememberMeCookie = ((WebRequest) getRequest()).getCookie("rememberme");
			add(new Label("chboxRemember", new StringResourceModel("chbox.rememberMe", this, null)));
			rememberMeCkb = new CheckBox(REMEMBER_ME_ID, new Model<Boolean>());
			if (rememberMeCookie != null && Boolean.valueOf(rememberMeCookie.getValue())) {
				rememberMeCkb.setModelObject(true);
			}

			add(rememberMeCkb);

			add(new IFormValidator() {

				private static final long serialVersionUID = 2966025224775093365L;

				@Override
	            public FormComponent<?>[] getDependentFormComponents() {
		            return new FormComponent<?>[] {usernameField, passwordField, rememberMeCkb};
	            }

				@Override
	            public void validate(Form<?> form) {
					if (Strings.isEmpty(usernameField.getConvertedInput()) ||
						Strings.isEmpty(passwordField.getConvertedInput())) {
						return;
					}

					UserSession us = (UserSession) getSession();

					byte[] passHash = null;
					passHash = Util.generateSha(passwordField.getConvertedInput());

					boolean status = us.authenticate(usernameField.getConvertedInput(), passHash);

					if (status) {
						if (rememberMeCkb.getConvertedInput()) {
							((WebResponse) getResponse()).addCookie(UserSession.createCookie(UserSession.COOKIE_USERNAME, usernameField.getConvertedInput()));
							try {
								String passHashStr = URLEncoder.encode((new String(passHash)), "UTF-8");
								((WebResponse) getResponse()).addCookie(UserSession.createCookie(UserSession.COOKIE_PASSWORD, passHashStr));
							} catch (Exception e) {
								log.error(e);
							}
						}

						((WebResponse) getResponse()).addCookie(UserSession.createCookie("rememberme", String.valueOf(rememberMeCkb.getConvertedInput())));
					} else {
						error(new StringResourceModel("not_found_message", LoginForm.this, null));
						onLoginFailed();
					}
	            }
			});
		}

		/**
		 * syzdava login button
		 * Moje da se Override-ne i da se izpolzva Ajax
		 * @param id
		 * @return
		 */
		protected Button createLoginButton(String id) {
			return new Button(id);
		}

		protected abstract void onLoginFailed();
	}
}
