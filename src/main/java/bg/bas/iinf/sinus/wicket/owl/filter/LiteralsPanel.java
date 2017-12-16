package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * spisyk s literali i filteringSelect
 *
 * @author hok
 *
 */
public abstract class LiteralsPanel extends GenericPanel<String> implements EditableFilterElement {

	private static final long serialVersionUID = -2526649421155258520L;

	public LiteralsPanel(String id, IModel<String> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setRenderBodyOnly(false);
		setOutputMarkupId(true);

		add(new DropDownChoice<String>("literal", getModel(), createList()));
	}

	@Override
	public String getTextValue() {
		return getModelObject();
	}

	protected abstract IModel<List<String>> createList();
}
