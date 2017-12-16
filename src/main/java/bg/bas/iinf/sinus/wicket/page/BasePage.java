package bg.bas.iinf.sinus.wicket.page;

import javascript.JS;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;

import bg.bas.iinf.sinus.wicket.auth.MenuBarPanel;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.CustomModalWindow;
import bg.bas.iinf.sinus.wicket.common.IModalWindowContainer;
import bg.bas.iinf.sinus.wicket.common.NotificationComponent;
import css.CSS;

/**
 * bazova stranica (header i footer)
 *
 * @author hok
 *
 */
public abstract class BasePage extends WebPage implements IModalWindowContainer {

    private static final long serialVersionUID = -937083264198161394L;

    private static final String TRY_AUTO_LOGIN = "login";
    private static final String IS_EMBEDDED_PARAM = "embed";

	private CustomModalWindow mainModalWindow;

	public abstract IModel<String> getPageTitle();

	public abstract IModel<String> getDescription();

	public BasePage() {
		super();
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
		if (!UserSession.get().isEmbedded()) {
			UserSession.get().seIsEmbedded(getPageParameters().getNamedKeys().contains(IS_EMBEDDED_PARAM));
		}
	}

	protected BasePage(final IModel<?> model) {
		super(model);
	}

	@Override
    protected void onInitialize() {
		super.onInitialize();

		if (getPageParameters().getNamedKeys().contains(TRY_AUTO_LOGIN)) {
			UserSession.get().tryAutoLogin((WebRequest) getRequest(), (WebResponse) getResponse());
			getPageParameters().remove(TRY_AUTO_LOGIN);
		}

	    addOrReplace(new Label("page_title", getPageTitle()));

		addOrReplace(new Label("page_description", "").add(new AttributeAppender("content", getDescription(), " ")));

		add(new MenuBarPanel("menu_bar_container") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!UserSession.get().isEmbedded());
			}
		});

		add(new NotificationComponent("notifications"));

		StatelessForm<Void> mainModalForm = new StatelessForm<Void>("main_modal_form");
		add(mainModalForm);
		mainModalForm.add(mainModalWindow = new CustomModalWindow("main_modal_window"));

		if (getPageTitle() != null && !UserSession.get().isEmbedded()) {
			add(new Label("big_title", getPageTitle()));
		} else {
			add(new EmptyPanel("big_title").setVisible(false));
		}
	}

	@Override
    public ModalWindow getModalWindow() {
		return mainModalWindow;
	}

	@Override
    public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		response.renderCSSReference(new PackageResourceReference(CSS.class, "wicket.css"));
		response.renderCSSReference(new PackageResourceReference(CSS.class, "main.css"));
		response.renderCSSReference(new PackageResourceReference(CSS.class, "dojo.css"));

		response.renderJavaScriptReference(new PackageResourceReference(JS.class, "utils.js"));
	}
}

