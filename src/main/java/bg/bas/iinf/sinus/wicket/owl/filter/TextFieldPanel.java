package bg.bas.iinf.sinus.wicket.owl.filter;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;

/**
 * element ot filtyra za vyvejdane na svoboden text
 * @author hok
 *
 */
public class TextFieldPanel extends GenericPanel<String> implements EditableFilterElement {

	private static final long serialVersionUID = -2526649421155258520L;

	public TextFieldPanel(String id, Model<String> model) {
		super(id, model);
	}

    @Override
	protected void onInitialize() {
		super.onInitialize();

		add(new TextField<String>("text_field", getModel()));
	}

	@Override
    public String getTextValue() {
	    return getDefaultModelObjectAsString();
    }
}
