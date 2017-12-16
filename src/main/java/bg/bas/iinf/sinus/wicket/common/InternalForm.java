package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.model.IModel;

/**
 * tazi forma e prednaznachena za nest-vane
 * ne se obrabotva pri submit na roditelskata forma
 * @author hok
 *
 * @param <T>
 */
public class InternalForm<T> extends Form<T> implements IFormVisitorParticipant {

	private static final long serialVersionUID = -8697799795432614279L;

	public InternalForm(String id) {
		super(id);
	}

	public InternalForm(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public boolean processChildren() {
		IFormSubmitter submitter = getRootForm().findSubmittingButton();
		if (submitter == null) {
			return true;
		}

		return submitter.getForm() == this;
	}
}
