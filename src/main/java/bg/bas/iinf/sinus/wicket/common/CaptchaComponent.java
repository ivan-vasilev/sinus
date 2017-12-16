package bg.bas.iinf.sinus.wicket.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * captcha
 * @author hok
 *
 */
public class CaptchaComponent extends Panel {

	private static final long serialVersionUID = 3254684693496372444L;

	private static final String CAPTCHA_ID = "captcha_image";
	private static final String CAPTCHA_PASSWORD = "captcha_password";

	private String generatedPassword;
	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}

	private String enteredPassword;

	public CaptchaComponent(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

        RequiredTextField<String> password = new RequiredTextField<String>(CAPTCHA_PASSWORD, new PropertyModel<String>(this, "enteredPassword"))
        {
			private static final long serialVersionUID = 1L;

			@Override
        	protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("value", "");
            }
        };

        password.add(new IValidator<String>() {

			private static final long serialVersionUID = 2966025224775093365L;

			@Override
    		public void validate(IValidatable<String> validatable) {
    			String value = validatable.getValue();
    			if (!generatedPassword.equalsIgnoreCase(value)) {
    				ValidationError error = new ValidationError();
    				error.addMessageKey("captcha.error");
    				validatable.error(error);
    			}
    		}
        });
        password.setOutputMarkupId(true);
        password.setLabel(new StringResourceModel("captcha.password", CaptchaComponent.this, null));
        add(password);

		add(new DisablingAjaxLink<Void>("refresh") {

			private static final long serialVersionUID = 257347444735386258L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				CaptchaComponent.this.replace(createCaptcha(CAPTCHA_ID));
				target.add(CaptchaComponent.this.get(CAPTCHA_ID), CaptchaComponent.this.get(CAPTCHA_PASSWORD));
			}
		});
	}

	public String getEnteredPassword() {
		return enteredPassword;
	}

	public void setEnteredPassword(String enteredPassword) {
		this.enteredPassword = enteredPassword;
	}

	@Override
	protected void onBeforeRender() {
        addOrReplace(createCaptcha(CAPTCHA_ID));

        super.onBeforeRender();
	}

	protected NonCachingImage createCaptcha(String id) {
		NonCachingImage result = new NonCachingImage(id, new CaptchaImageResource(new PropertyModel<String>(this, "generatedPassword"))) {

            private static final long serialVersionUID = 1349928194371705300L;

			@Override
			protected void onBeforeRender() {
				if (getApplication().usesDevelopmentConfig()) {
					generatedPassword = RandomStringUtils.randomAlphanumeric(1);
				} else {
					generatedPassword = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
				}

				super.onBeforeRender();
			}
		};
		result.setOutputMarkupId(true);
		return result;
	}
}
