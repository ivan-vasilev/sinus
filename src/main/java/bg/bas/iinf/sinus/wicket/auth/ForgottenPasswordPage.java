package bg.bas.iinf.sinus.wicket.auth;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.application.URLConstants;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.common.Util;
import bg.bas.iinf.sinus.wicket.page.BasePage;
import bg.bas.iinf.sinus.wicket.staticpages.MessagePage;

/**
 * stranica za zabravena parola
 * @author hok
 *
 */
public class ForgottenPasswordPage extends BasePage {

    private static final long serialVersionUID = 4700964012931419207L;

	public ForgottenPasswordPage() {
		super();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		UserSession us = (UserSession) getSession();
	    if (us.getUserId() != null) {
	    	throw new RestartResponseException(Application.get().getHomePage());
	    }

		add(new ForgottenPasswordForm("forgotten_password_form"));
	}

	private static class ForgottenPasswordForm extends ErrorHighlightingForm<Users> {

		private static final long serialVersionUID = 867492200791785600L;

		private RequiredTextField<String> emailField;
		private Label feedbackLabel;

		public ForgottenPasswordForm(String id) {
			super(id);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			setOutputMarkupId(true);

			feedbackLabel = new Label("feedback");
			add(feedbackLabel);

			emailField = new RequiredTextField<String>("email", new Model<String>());
			emailField.add(EmailAddressValidator.getInstance());
			add(emailField);

			add(new IFormValidator() {

                private static final long serialVersionUID = 7962759000187968699L;

				@Override
				public void validate(Form<?> form) {
					Users user = UsersHome.findByUsernameOrEmail(ScopedEntityManagerFactory.getEntityManager(), emailField.getConvertedInput());

					if (user == null) {
						form.error(new StringResourceModel("user_not_found_message", ForgottenPasswordForm.this, null).getString());
					}
				}

				@Override
				public FormComponent<?>[] getDependentFormComponents() {
					return new FormComponent[] { emailField };
				}
			});

			add(new Button("send_button"));
		}

		@Override
		public void onSubmit() {
			Users user = UsersHome.findByUsernameOrEmail(ScopedEntityManagerFactory.getEntityManager(), emailField.getConvertedInput());

			if (user != null) {
				String randomPassword = RandomStringUtils.randomAlphanumeric(10);

				user.setPasswordHash(Util.generateSha(randomPassword));
				UsersHome.persistOrMergeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), user);

				send(getApplication(), Broadcast.EXACT, new ForgottenPasswordEP(user, randomPassword));

				setResponsePage(new MessagePage(new StringResourceModel("found_message", this, null)));
			}
		}

		@Override
        public void onError() {
			if (hasErrorMessage()) {
				feedbackLabel.setDefaultModel(new Model<String>(getFeedbackMessage().getMessage().toString()));
			}
		}
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("forgotten_password", this, null);
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("forgotten_password", this, null);
	}

	@Override
    public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavaScript("dojo.require('dijit.Tooltip');", URLConstants.REQUIRE_TOOLTIP);
	}

	public static class ForgottenPasswordEP {

		private Users user;
		private String newPassword;

		public ForgottenPasswordEP(Users user, String newPassword) {
			super();
			this.user = user;
			this.newPassword = newPassword;
		}

		public Users getUser() {
			return user;
		}

		public String getNewPassword() {
			return newPassword;
		}
	}
}