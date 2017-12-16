package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 * Ajax link s potvyrjdenie
 * @author hok
 *
 * @param <T>
 */
public abstract class ConfirmAjaxLink<T> extends AjaxLink<T> {

	private static final long serialVersionUID = 6046185583789001887L;

	private IModel<String> confirm;

	public ConfirmAjaxLink(String id, IModel<T> model, IModel<String> confirm) {
		super(id, model);
		this.confirm = confirm;
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {

			private static final long serialVersionUID = 47742850042283696L;

			@Override
			public CharSequence preDecorateScript(CharSequence script) {
				return "if(!confirm('" + confirm.getObject() + "')) return false;" + script;
			}
		};
	}

}
