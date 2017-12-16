package bg.bas.iinf.sinus.wicket.auth;

import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.common.DisablingAjaxLink;
import bg.bas.iinf.sinus.wicket.common.DojoUtils;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.common.IModalWindowContainer;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.common.Util;
import bg.bas.iinf.sinus.wicket.model.UserLDM;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * Stranica za potrebitelski nastroiki (parola, email etc)
 * @author hok
 *
 */
@AuthorizeInstantiation({ AuthRoles.ADMIN_CONST, AuthRoles.ANNOTATOR_CONST, AuthRoles.LIBRARIAN_CONST, AuthRoles.STUDENT_CONST })
public class EditUserPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// Admin mail
		add(new AjaxLink<Void>("admin_mail_link") {
			private static final long serialVersionUID = -1673816731522718326L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ModalWindow window = findParent(IModalWindowContainer.class).getModalWindow();
				AdminMailModalFragment adminMailFragment = new AdminMailModalFragment(window.getContentId(), "admin_mail_fragment", EditUserPage.this);
				adminMailFragment.add(AttributeModifier.replace("style", "min-width: 230px;"));
				window.setContent(adminMailFragment);
				window.setTitle(new StringResourceModel("admin_mail_field", this, null));
				window.show(target);
			}
		});

		// parola
		add(new AjaxLink<Void>("show_change_password_window") {
			private static final long serialVersionUID = -1673816731522718326L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ModalWindow modal = findParent(IModalWindowContainer.class).getModalWindow();
				modal.setTitle(new StringResourceModel("change_password", this, null));
				modal.setContent(new ChangePasswordModalFragment(modal.getContentId(),"change_password_fragment", EditUserPage.this) {

                    private static final long serialVersionUID = 3766666560309088661L;

					@Override
					protected void onFormSubmitted(AjaxRequestTarget target) {
						findParent(IModalWindowContainer.class).getModalWindow().close(target);
					}

					@Override
					protected void onFormCancelled(AjaxRequestTarget target) {
						findParent(IModalWindowContainer.class).getModalWindow().close(target);
					}
				});
				modal.show(target);
			}
		});
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("profile", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("profile", this, null);
	}

	private static class AdminMailModalFragment extends Fragment {

		private static final long serialVersionUID = -6317846543873802051L;

		public AdminMailModalFragment(String id, String markupId, MarkupContainer markupProvider) {
			super(id, markupId, markupProvider);
		}

        @Override
		protected void onInitialize() {
			super.onInitialize();

			Form<Users> form = new ErrorHighlightingForm<Users>("form", new UserLDM(UserSession.get().getUser()));
			form.setOutputMarkupId(true);
			add(form);

			RequiredTextField<String> adminMail = new RequiredTextField<String>("admin_mail_field", new PropertyModel<String>(form.getModel(), "email"));
			adminMail.add(EmailAddressValidator.getInstance());
			adminMail.add(StringValidator.maximumLength(255));
			form.add(adminMail);

			AjaxButton submit = new AjaxButton("submit", form) {

				private static final long serialVersionUID = 3952824467594739527L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), form.getModelObject());
					send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
					findParent(IModalWindowContainer.class).getModalWindow().close(target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(form);
				}
			};
			form.add(submit);

			form.add(new DisablingAjaxLink<Void>("close") {

				private static final long serialVersionUID = 3952824467594739527L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					ModalWindow mw = (ModalWindow) AdminMailModalFragment.this.getParent();
					mw.close(target);
				}
			});
		}
	}

	private static abstract class ChangePasswordModalFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public ChangePasswordModalFragment(String id, String markupId, MarkupContainer markupProvider) {
			super(id, markupId, markupProvider);
		}

	    @Override
		protected void onInitialize() {
			super.onInitialize();

			setOutputMarkupId(true);

			UserLDM model = new UserLDM(UserSession.get().getUser());
	        final ErrorHighlightingForm<Users> form = new ErrorHighlightingForm<Users>("edit_password_form", model);
			form.setOutputMarkupId(true);
			add(form);

			EditPasswordFragment passwordFragment = new EditPasswordFragment("password", "password_fragment", this, model) {

	            private static final long serialVersionUID = -1305397650208210836L;

				@Override
				protected void onInitialize() {
					super.onInitialize();

					PasswordTextField oldPasswordField = new PasswordTextField("old_password", new Model<String>());

					oldPasswordField.add(new IValidator<String>() {

						private static final long serialVersionUID = 2966025224775093365L;

						@Override
						public void validate(IValidatable<String> validatable) {
							byte[] newValue = Util.generateSha(validatable.getValue());
							if (!Arrays.equals(form.getModelObject().getPasswordHash(), newValue)) {
								ValidationError error = new ValidationError();
								error.addMessageKey("wrong_password");
								validatable.error(error);
							}
						}
					});

					add(oldPasswordField);
				}
			};
			form.add(passwordFragment);

			AjaxButton sendButton = new AjaxButton("change_password_button") {

				private static final long serialVersionUID = 7737945087600561134L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					DojoUtils.validateForm(target, ChangePasswordModalFragment.this);
					target.add(ChangePasswordModalFragment.this);

					NotificationEventPayload nep = new NotificationEventPayload(target, new ResourceModel("incorrect_data_error"), NotificationType.ERROR, null);
					send(getPage(), Broadcast.BREADTH, nep);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Users user = (Users) form.getModelObject();
					UsersHome.persistOrMergeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), user);
					send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));

					onFormSubmitted(target);
				}
			};
			sendButton.setOutputMarkupId(true);
			form.add(sendButton);

			form.add(new AjaxLink<Void>("close") {

	            private static final long serialVersionUID = -7133898732057649059L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					onFormCancelled(target);
				}
			});
		}

		protected abstract void onFormSubmitted(AjaxRequestTarget target);
		protected abstract void onFormCancelled(AjaxRequestTarget target);
	}
}
