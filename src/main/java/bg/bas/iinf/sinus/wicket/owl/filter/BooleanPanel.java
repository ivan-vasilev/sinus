package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * panel za filtyr na bulevi stoinosti
 * @author hok
 *
 */
public class BooleanPanel extends GenericPanel<Boolean> implements EditableFilterElement {

	private static final long serialVersionUID = -2526649421155258520L;

	public BooleanPanel(String id, IModel<Boolean> model) {
		super(id, model);
	}

    @Override
	protected void onInitialize() {
		super.onInitialize();

		List<Boolean> conditionsList = new ArrayList<Boolean>();
		conditionsList.add(true);
		conditionsList.add(false);

		ChoiceRenderer<Boolean> conditionRenderer = new ChoiceRenderer<Boolean>() {

			private static final long serialVersionUID = -4547508967714768964L;

			@Override
			public Object getDisplayValue(Boolean object) {
				return (new StringResourceModel(object == true ? "true" : "false", BooleanPanel.this, null)).getString();
			}
		};

		DropDownChoice<Boolean> booleanChoice = new DropDownChoice<Boolean>("boolean_choice", getModel(), conditionsList, conditionRenderer);
		booleanChoice.setNullValid(true);
		add(booleanChoice);
	}

	@Override
    public String getTextValue() {
		if (getModelObject() != null) {
			return getModelObject() ? new StringResourceModel("true", this, null).getString() : new StringResourceModel("false", this, null).getString();
		}

		return "";
    }
}
