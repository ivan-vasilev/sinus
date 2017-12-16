package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import bg.bas.iinf.sinus.wicket.model.owl.FromToNumber;

/**
 * vyvejdane na chislo
 *
 * @author hok
 *
 * @param <T>
 */
public class NumberPanel<T extends Number> extends Panel implements EditableFilterElement {

	private static final long serialVersionUID = -7055660737796638534L;

	private enum Type { EXACT_VALUE, RANGE };

	private static final String NUMBER_ID = "number";

	public NumberPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@SuppressWarnings("unchecked")
    @Override
	protected void onInitialize() {
		super.onInitialize();

		setOutputMarkupId(true);

		List<Integer> choices = new LinkedList<Integer>();
		choices.add(Type.EXACT_VALUE.ordinal());
		choices.add(Type.RANGE.ordinal());

		ChoiceRenderer<Integer> choiceRenderer = new ChoiceRenderer<Integer>() {

			private static final long serialVersionUID = -4547508967714768964L;

			@Override
			public Object getDisplayValue(Integer object) {
				if (object.equals(0)) {
					return (new StringResourceModel("exact_value", NumberPanel.this, null)).getString();
				}

				return (new StringResourceModel("range_values", NumberPanel.this, null)).getString();
			}
		};

		Type type = getDefaultModelObject() != null && getDefaultModelObject() instanceof FromToNumber<?> ? Type.RANGE : Type.EXACT_VALUE;

		DropDownChoice<Integer> numberType = new DropDownChoice<Integer>("number_type", new Model<Integer>(type.ordinal()), choices, choiceRenderer);
		add(numberType);
		numberType.setNullValid(false);
		numberType.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = -1171121345382374736L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				send(NumberPanel.this, Broadcast.EXACT, new TypeChangedEP(target, Type.values()[(Integer) getFormComponent().getModelObject()]));
			}
		});

		if (type.equals(Type.RANGE)) {
			setDefaultModelObject(new FromToNumber<T>());
			add(new RangeValuesFragment(NUMBER_ID, "range_values", this, (IModel<FromToNumber<T>>) getDefaultModel()));
			numberType.setDefaultModelObject(Type.RANGE.ordinal());
		} else {
			add(new ExactValueFragment(NUMBER_ID, "exact_value_fragment", this, (IModel<T>) getDefaultModel()));
			numberType.setDefaultModelObject(Type.EXACT_VALUE.ordinal());
		}
	}

	@SuppressWarnings("unchecked")
    @Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof TypeChangedEP) {
			TypeChangedEP payload = (TypeChangedEP) event.getPayload();
			if (Type.EXACT_VALUE.equals(payload.getType())) {
				setDefaultModelObject(null);
				replace(new ExactValueFragment(NUMBER_ID, "exact_value_fragment", this, (IModel<T>) getDefaultModel()));
			} else {
				setDefaultModelObject(new FromToNumber<T>());
				replace(new RangeValuesFragment(NUMBER_ID, "range_values_fragment", this, (IModel<FromToNumber<T>>) getDefaultModel()));
			}

			payload.getTarget().add(this);
		}
	}

	private class ExactValueFragment extends Fragment {

        private static final long serialVersionUID = -6910921481514462702L;

		public ExactValueFragment(String id, String markupId, MarkupContainer markupProvider, IModel<T> model) {
	        super(id, markupId, markupProvider, model);
        }

		@SuppressWarnings("unchecked")
        @Override
		protected void onInitialize() {
			super.onInitialize();

			add(new TextField<T>("exact_value", (IModel<T>) getDefaultModel()));
		}
	}

	private class RangeValuesFragment extends Fragment {

        private static final long serialVersionUID = -6910921481514462702L;

		public RangeValuesFragment(String id, String markupId, MarkupContainer markupProvider, IModel<FromToNumber<T>> model) {
	        super(id, markupId, markupProvider, model);
        }

        @Override
		protected void onInitialize() {
			super.onInitialize();

			add(new TextField<T>("from", new PropertyModel<T>(getDefaultModel(), "from")));
			add(new TextField<T>("to", new PropertyModel<T>(getDefaultModel(), "to")));
		}
	}

	public static class TypeChangedEP {

		private AjaxRequestTarget target;
		private Type type;

		public TypeChangedEP(AjaxRequestTarget target, Type type) {
			super();
			this.target = target;
			this.type = type;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public void setTarget(AjaxRequestTarget target) {
			this.target = target;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}
	}

	@Override
    public String getTextValue() {
		if (getDefaultModelObject() != null) {
			if (getDefaultModelObject() instanceof FromToNumber) {
				return new StringResourceModel("text_value", this, getDefaultModel()).getString();
			}

			return getDefaultModelObjectAsString();
		}

		return "";
    }
}
