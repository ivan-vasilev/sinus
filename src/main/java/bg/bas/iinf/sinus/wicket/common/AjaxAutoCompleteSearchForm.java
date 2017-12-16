package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * autocomplete forma s ajax submit
 * @author hok
 *
 * @param <T>
 */
public abstract class AjaxAutoCompleteSearchForm<T> extends AbstractAutoCompleteSearchForm<T> {

	private static final long serialVersionUID = -2255415309366459144L;

	public AjaxAutoCompleteSearchForm(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new IndicatingAjaxButton("search_button", new ResourceModel("search_button")) {

			private static final long serialVersionUID = 7218731720312825394L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AjaxAutoCompleteSearchForm.this.onSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				AjaxAutoCompleteSearchForm.this.onError(target);
			}
		});
	}

	protected abstract void onSubmit(AjaxRequestTarget target);

	protected void onError(AjaxRequestTarget target) {
		if (hasErrorMessage()) {
			NotificationEventPayload nep = new NotificationEventPayload(target, getFeedbackMessage().getMessage().toString(), NotificationType.ERROR, null);
			send(getPage(), Broadcast.DEPTH, nep);
		} else {
			NotificationEventPayload nep = new NotificationEventPayload(target, new ResourceModel("general_error"), NotificationType.ERROR, null);
			send(getPage(), Broadcast.DEPTH, nep);
		}
	}
}
