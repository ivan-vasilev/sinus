package bg.bas.iinf.sinus.wicket.staticpages;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * stranica za pokazvane na html template-i
 * @author hok
 *
 */
public abstract class HtmlTemplatePage extends BasePage {

    private static final long serialVersionUID = -7443173732922223233L;

    public static final int KEY_PARAM =  0;

	public HtmlTemplatePage(IModel<?> model) {
	    super(model);
    }

	public HtmlTemplatePage() {
		super();
	}

	public HtmlTemplatePage(PageParameters parameters) {
	    super(parameters);
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(createComponent("template"));
	}

	protected abstract Component createComponent(String id);
}