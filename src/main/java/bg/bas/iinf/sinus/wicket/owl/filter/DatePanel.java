package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import bg.bas.iinf.sinus.wicket.model.owl.FromToNumber;

/**
 * panel za filtrirane po data
 * @author hok
 *
 */
public class DatePanel extends Panel implements EditableFilterElement {

	private static final long serialVersionUID = -2526649421155258520L;

	private static final String ID = "date";

	private enum Type { EXACT_VALUE, RANGE };

	public DatePanel(String id, IModel<?> model) {
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
					return (new StringResourceModel("exact_value", DatePanel.this, null)).getString();
				}

				return (new StringResourceModel("range_values", DatePanel.this, null)).getString();
			}
		};

		Type type = getDefaultModelObject() != null && getDefaultModelObject() instanceof FromToNumber<?> ? Type.RANGE : Type.EXACT_VALUE;

		DropDownChoice<Integer> numberType = new DropDownChoice<Integer>("date_type", new Model<Integer>(type.ordinal()), choices, choiceRenderer);
		add(numberType);
		numberType.setNullValid(false);
		numberType.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = -1171121345382374736L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				send(DatePanel.this, Broadcast.EXACT, new TypeChangedEP(target, Type.values()[(Integer) getFormComponent().getModelObject()]));
			}
		});

		if (type.equals(Type.RANGE)) {
			setDefaultModelObject(new FromToNumber<Date>());
			add(new RangeValuesFragment(ID, "range_values", this, (IModel<FromToNumber<Date>>) getDefaultModel()));
			numberType.setDefaultModelObject(Type.RANGE.ordinal());
		} else {
			add(new ExactValueFragment(ID, "exact_value_fragment", this, (IModel<Date>) getDefaultModel()));
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
				replace(new ExactValueFragment(ID, "exact_value_fragment", this, (IModel<Date>) getDefaultModel()));
			} else {
				setDefaultModelObject(new FromToNumber<Date>());
				replace(new RangeValuesFragment(ID, "range_values_fragment", this, (IModel<FromToNumber<Date>>) getDefaultModel()));
			}

			payload.getTarget().add(this);
		}
	}

	private class ExactValueFragment extends Fragment {

        private static final long serialVersionUID = -6910921481514462702L;

		public ExactValueFragment(String id, String markupId, MarkupContainer markupProvider, IModel<Date> model) {
	        super(id, markupId, markupProvider, model);
        }

		@SuppressWarnings("unchecked")
        @Override
		protected void onInitialize() {
			super.onInitialize();

			// ot
	        DateTextField dateTextField = DateTextField.forDatePattern("exact_date", (IModel<Date>) getDefaultModel(), "dd.MM.yy");
			add(dateTextField);

			// komponent za izbor na data
			dateTextField.add(new DatePicker());
		}
	}

	private class RangeValuesFragment extends Fragment {

        private static final long serialVersionUID = -6910921481514462702L;

		public RangeValuesFragment(String id, String markupId, MarkupContainer markupProvider, IModel<FromToNumber<Date>> model) {
	        super(id, markupId, markupProvider, model);
        }

        @Override
		protected void onInitialize() {
			super.onInitialize();

			StyleDateConverter dateConverter = new StyleDateConverter("S-", true) {

				private static final long serialVersionUID = -3247788374717061186L;

				@Override
				public Locale getLocale() {
					return getSession().getLocale();
				}
			};

			// ot
	        DateTextField fromTextField = new DateTextField("from", new PropertyModel<Date>(getDefaultModel(), "from"), dateConverter);
			add(fromTextField);

			// komponent za izbor na data
			fromTextField.add(new DatePicker());

			// do
	        DateTextField toTextField = new DateTextField("to", new PropertyModel<Date>(getDefaultModel(), "to"), dateConverter);
			add(toTextField);

			// komponent za izbor na data
			toTextField.add(new DatePicker());
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
