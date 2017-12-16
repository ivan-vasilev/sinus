package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 * Disabling Ajax link
 * @author hok
 *
 * @param <T>
 */
public abstract class DisablingAjaxLink<T> extends AjaxLink<T> {

    private static final long serialVersionUID = -7817810863719722935L;

	public DisablingAjaxLink(String id, IModel<T> model) {
		super(id, model);
	}

	public DisablingAjaxLink(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * disable/enable pri natiskane
	 *
	 * @see org.apache.wicket.ajax.markup.html.form.AjaxButton#getAjaxCallDecorator()
	 */
	@Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new DisablingLinkAjaxCallDecorator();
	}
}
