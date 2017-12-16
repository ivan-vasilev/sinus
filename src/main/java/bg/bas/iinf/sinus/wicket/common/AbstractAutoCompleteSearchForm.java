package bg.bas.iinf.sinus.wicket.common;

import java.util.Iterator;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * bazov klas za forma s autocomplete textovo pole i submit buton
 * @author hok
 *
 * @param <T>
 */
public abstract class AbstractAutoCompleteSearchForm<T> extends InternalForm<T> {

	private static final long serialVersionUID = 6826202659543159542L;

	protected AutoCompleteTextField<String> searchField;

	public AbstractAutoCompleteSearchForm(String id) {
		super(id);
	}

	public AbstractAutoCompleteSearchForm(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		searchField = new AutoCompleteTextField<String>("search_term", new Model<String>()) {

			private static final long serialVersionUID = -6818846159787491537L;

			@Override
			protected Iterator<String> getChoices(String input) {
				return AbstractAutoCompleteSearchForm.this.getChoices(input);
			}
		};
		searchField.add(StringValidator.maximumLength(255));
		add(searchField);
	}

	protected abstract Iterator<String> getChoices(String input);
}
