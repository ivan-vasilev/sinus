package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * forma, koqto dobavq cherven marker na komponentite, koito imat greshki
 * @author hok
 *
 * @param <T>
 */
public class ErrorHighlightingForm<T> extends Form<T> {

	private static final long serialVersionUID = 5021779256858783349L;

	private FormErrorVisitor errorVisitor = new FormErrorVisitor();

	public ErrorHighlightingForm(String id) {
		super(id);
	}

	public ErrorHighlightingForm(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onBeforeRender() {
		visitFormComponents(errorVisitor);
		super.onBeforeRender();
	}
}
