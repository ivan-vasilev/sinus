package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.EmptyPanel;

/**
 * modalen prozorec s nqkoi nastroiki
 *
 * @author hok
 *
 */
public class CustomModalWindow extends ModalWindow {

	private static final long serialVersionUID = 2765002639332302593L;

	public CustomModalWindow(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setAutoSize(true);

		setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = -154870878962186190L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		// za da se zatvarq pri natiskane na esc
		add(new AbstractDefaultAjaxBehavior() {

            private static final long serialVersionUID = -7521232916573570861L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				close(target);
				target.appendJavaScript(getisShownJSVarName() + " = false");
			}

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				response.renderJavaScript("var " + getisShownJSVarName() + " = false;", getisShownJSVarName());
				String js = String.format("dojo.connect(\"onkeypress\", function(e) { if (e.charOrCode == dojo.keys.ESCAPE && %s == true) {%s = true; %s;} } );", getisShownJSVarName(), getisShownJSVarName(), getCallbackScript());
				response.renderJavaScript(js, "connect" + getMarkupId(true));
			}
		});
	}

	@Override
	public void show(AjaxRequestTarget target) {
		target.appendJavaScript("Wicket.Window.unloadConfirmation = false;");
		target.appendJavaScript(getisShownJSVarName() + " = true;");
		super.show(target);
	}

	@Override
	public void close(AjaxRequestTarget target) {
		if (isShown()) {
			setContent(new EmptyPanel(getContentId()));
			super.close(target);
			target.appendJavaScript(getisShownJSVarName() + " = false");
		}
	}

	protected String getisShownJSVarName() {
		return "isShown" + getMarkupId(true);
	}
}
