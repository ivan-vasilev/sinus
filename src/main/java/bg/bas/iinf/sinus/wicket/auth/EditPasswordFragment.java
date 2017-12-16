package bg.bas.iinf.sinus.wicket.auth;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.StringValidator;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.wicket.common.Util;

/**
 * fragment za redaktirane na parola
 * @author hok
 *
 */
public class EditPasswordFragment extends Fragment {

	private static final long serialVersionUID = -3735817190377318588L;

	private PasswordTextField passwordField;
	private PasswordTextField repeatPasswordField;

	public EditPasswordFragment(String id, String markupId, final MarkupContainer markupProvider, IModel<Users> model) {
		super(id, markupId, markupProvider, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		passwordField = new PasswordTextField("password", new UserPasswordPropertyModel((Users) getDefaultModelObject()));
		passwordField.add(StringValidator.minimumLength(6));
		passwordField.add(StringValidator.maximumLength(255));
		passwordField.setRequired(true);
		passwordField.setOutputMarkupId(true);
		add(passwordField);

		repeatPasswordField = new PasswordTextField("repeat_password", new Model<String>(""));
		repeatPasswordField.setRequired(true);
		repeatPasswordField.setOutputMarkupId(true);
		add(repeatPasswordField);
	}

	@Override
	public void onBeforeRender() {
		repeatPasswordField.getForm().add(new EqualPasswordInputValidator(passwordField, repeatPasswordField));
		super.onBeforeRender();
	}

	private static class UserPasswordPropertyModel extends Model<String> {

        private static final long serialVersionUID = -8055713284131657646L;

		Users user;
		public UserPasswordPropertyModel(Users user) {
	        super();
	        this.user = user;
        }

		@Override
		public void setObject(String object) {
			super.setObject(object);
			if (!Strings.isEmpty(object)) {
				user.setPasswordHash(Util.generateSha(object));
			}
		}
	}
}