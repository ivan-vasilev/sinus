package bg.bas.iinf.sinus.wicket.auth;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.entity.UsersConfirmation;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.application.URLConstants;
import bg.bas.iinf.sinus.wicket.common.CaptchaComponent;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.owl.filter.FilterPage;
import bg.bas.iinf.sinus.wicket.page.BasePage;
import bg.bas.iinf.sinus.wicket.staticpages.MessagePage;

/**
 * Stranica za registraciq na potrebitel
 * @author hok
 *
 */
public class RegistrationPage extends BasePage {

	private static final long serialVersionUID = 7870274132639391353L;

	private Label usernameExists;
	private RequiredTextField<String> username;
	private ErrorHighlightingForm<Users> form;
	private IndicatingAjaxButton submit;

	public RegistrationPage() {
	    super();
	    UserSession us = (UserSession) getSession();
	    if (us.getUserId() != null) {
	    	throw new RestartResponseException(FilterPage.class);
	    }
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Users user = new Users();

		IModel<Users> model = new Model<Users>(user);
		form = new ErrorHighlightingForm<Users>("registration_form", model);
		form.setOutputMarkupId(true);
		add(form);

		if (UserSession.get().getLoggedEmail() == null) {
			RegistrationEmailFragment mailFragment = new RegistrationEmailFragment("admin_mail", "admin_mail_fragment", this, form.getModel());
			mailFragment.setRenderBodyOnly(true);
			form.add(mailFragment);
		} else {
			form.add(new EmptyPanel("admin_mail").setRenderBodyOnly(true));
		}

		username = new RequiredTextField<String>("edit_username", new PropertyModel<String>(model, "name"));
		username.add(StringValidator.minimumLength(5));
		username.add(StringValidator.maximumLength(15));
		username.setOutputMarkupId(true);
		username.add(new IValidator<String>() {

			private static final long serialVersionUID = 2966025224775093365L;

			@Override
			public void validate(IValidatable<String> validatable) {
				if (username.getForm().isSubmitted()
				        && UsersHome.userNameExists(ScopedEntityManagerFactory.getEntityManager(), username.getConvertedInput())) {
					ValidationError error = new ValidationError();
					error.addMessageKey("error_username_exists");
					validatable.error(error);
				}
			}
		});

		username.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 5140589466448761084L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				boolean exists = UsersHome.userNameExists(ScopedEntityManagerFactory.getEntityManager(), username.getConvertedInput());

				Users user = (Users) getFormComponent().getForm().getModelObject();

				if (exists == true) {
					usernameExists.setDefaultModelObject(user.getName() + (getString("is_taken")));
					usernameExists.add(AttributeAppender.append("style", "color:#c00;"));
				} else {
					usernameExists.setDefaultModelObject(user.getName() + getString("is_free"));
					usernameExists.add(AttributeAppender.append("style", "color:green;"));
				}

				target.add(usernameExists);
			}
		});

		// toq pattern validator e za username - izkluchva vsichki whitespace
		// simvoli
		username.add(new PatternValidator("\\S+"));
		form.add(username);

		usernameExists = new Label("username_exists", new Model<String>(""));
		usernameExists.setOutputMarkupId(true);
		form.add(usernameExists);

		EditPasswordFragment passwordFragment = new EditPasswordFragment("password", "password_fragment", this, model);
		passwordFragment.setOutputMarkupId(false);
		form.add(passwordFragment);

		form.add(new CaptchaComponent("captcha"));

		submit = new IndicatingAjaxButton("create_user_button") {

			private static final long serialVersionUID = -1510036347558900806L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Users user = (Users) form.getModelObject();

				usernameExists.setDefaultModel(new Model<String>(""));

				boolean requireConfirm = UserSession.get().getLoggedEmail() == null;

				if (requireConfirm) {
					user.getUsersConfirmation().add(new UsersConfirmation());
				}

				user = UsersHome.persistOrMergeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), user);

				send(getApplication(), Broadcast.EXACT, new UserRegisteredEP(user));

				if (requireConfirm) {
					setResponsePage(new MessagePage(new StringResourceModel("create_success_require_confirmation", RegistrationPage.this, null)));
				} else {
					setResponsePage(new MessagePage(new StringResourceModel("create_success", RegistrationPage.this, null)));
				}

				// ako nqma nujda ot potvyrjdenie napravo se logva
				if (!requireConfirm) {
					((UserSession) getSession()).setUserId(user.getUsersId());
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				DojoUtils.validateForm(target, form);
				target.add(form);

				NotificationEventPayload nep = null;
				if (form.hasErrorMessage()) {
					nep = new NotificationEventPayload(target, form.getFeedbackMessage().getMessage().toString(), NotificationType.ERROR, null);
				} else {
					nep = new NotificationEventPayload(target, new ResourceModel("incorrect_data_error"), NotificationType.ERROR, null);
				}
				send(getPage(), Broadcast.DEPTH, nep);
			}
		};
		form.add(submit);
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("registration", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("registration", this, null);
	}

	@Override
	protected void configureResponse(final WebResponse response) {
		super.configureResponse(response);

		response.disableCaching();
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderJavaScript("dojo.require('dijit.Tooltip');", URLConstants.REQUIRE_TOOLTIP);
	}

	/**
	 * proverka dali syshtestvuva takyv email v bazata
	 * samo pri deployment configuraciq
	 * @author hok
	 *
	 */
	private static class RegistrationEmailFragment extends Fragment {

        private static final long serialVersionUID = 173184527738000044L;

        private Label emailExists;
    	protected RequiredTextField<String> adminMail;

		public RegistrationEmailFragment(String id, String markupId, MarkupContainer markupProvider, IModel<Users> model) {
	        super(id, markupId, markupProvider, model);
        }

		@Override
		protected void onInitialize() {
			super.onInitialize();

			adminMail = new RequiredTextField<String>("admin_mail_field", new PropertyModel<String>(getDefaultModel(), "email"));
			adminMail.add(EmailAddressValidator.getInstance());
			adminMail.add(StringValidator.maximumLength(255));
			add(adminMail);

			emailExists = new Label("email_exists", new Model<String>(""));
			emailExists.setOutputMarkupId(true);
			add(emailExists);

			adminMail.add(new IValidator<String>() {

				private static final long serialVersionUID = 2966025224775093365L;

				@Override
				public void validate(IValidatable<String> validatable) {
					if (adminMail.getForm().isSubmitted()
					        && UsersHome.userEmailExists(ScopedEntityManagerFactory.getEntityManager(), adminMail.getConvertedInput())) {
						validatable.error(new ValidationError().addMessageKey("error_email_exists"));
					}
				}
			});

			adminMail.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 5140589466448761084L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					boolean exists = UsersHome.userEmailExists(ScopedEntityManagerFactory.getEntityManager(), adminMail.getConvertedInput());

					if (exists == true) {
						emailExists.setDefaultModelObject(adminMail.getConvertedInput() + getString("is_taken"));
						emailExists.add(AttributeAppender.append("style", "color:#c00;"));
					} else {
						emailExists.setDefaultModelObject(adminMail.getConvertedInput() + getString("is_free"));
						emailExists.add(AttributeAppender.append("style", "color:green;"));
					}

					target.add(emailExists);
				}
			});
		}
	}

	/**
	 * Event payload za registraciq
	 *
	 * @author hok
	 *
	 */
	public static class UserRegisteredEP {
		private Users user;

		public UserRegisteredEP(Users user) {
			super();
			this.user = user;
		}

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}
	}
}
