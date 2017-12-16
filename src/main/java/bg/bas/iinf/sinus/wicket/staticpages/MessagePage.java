package bg.bas.iinf.sinus.wicket.staticpages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import bg.bas.iinf.sinus.wicket.common.ComponentsFragment;

/**
 * statichna stranica, koqto pokazva syobshtenie na ekrana
 * @author hok
 *
 */
public class MessagePage extends HtmlTemplatePage {

    private static final long serialVersionUID = -4449300308301715573L;

    public MessagePage() {
	    super();
    }

	public MessagePage(IModel<String> model) {
		super(model);
	}

	public MessagePage(PageParameters parameters) {
		super(parameters);
		setDefaultModel(new ResourceModel(parameters.get(HtmlTemplatePage.KEY_PARAM).toOptionalString()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return new ResourceModel("sinus");
	}

	@Override
	public IModel<String> getDescription() {
		return new ResourceModel("sinus");
	}

	@Override
    protected Component createComponent(String id) {
	    return new ComponentsFragment(id, "message_fragment", this, new Label("message_label", getDefaultModel()));
    }
}
