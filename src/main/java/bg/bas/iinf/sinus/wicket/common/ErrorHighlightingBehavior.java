package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * tozi behavior dobavq cherven marker do komponentite ot formata, koito imat greshki
 * @author hok
 *
 */
public class ErrorHighlightingBehavior extends Behavior {

	private static final long serialVersionUID = -8442890907480026215L;

	@Override
    public void afterRender(Component c) {

		FormComponent<?> fc = (FormComponent<?>) c;
		if (!fc.isValid()) {
			// pokazva greshkata
			String error = String.format("<span style=\"display:inline-block;\"><span class=\"error_exclamation_mark\" id=\"%s\" title=\"%s\"></span></span>",
										fc.getMarkupId() + "err", fc.getFeedbackMessage().getMessage().toString());
			fc.getResponse().write(error);
		}
	}

	@Override
    public void onComponentTag(Component c, ComponentTag tag) {

		FormComponent<?> fc = (FormComponent<?>) c;
		if (!fc.isValid()) {
			// ocvetqva v cherveno
			tag.put("style", "background-color:#FFD0D0");
		}
	}
}
