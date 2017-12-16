package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * pri natiskane na ajax buton/link toi se disable-va
 * pri uspeh ili greshka se enable-va
 *
 * @author hok
 *
 */
public class DisablingLinkAjaxCallDecorator implements IAjaxCallDecorator {

    private static final long serialVersionUID = 8253527600031889722L;

	@Override
	public CharSequence decorateScript(Component component, CharSequence script) {
		return "if (this.disabled != null && this.disabled == true) return false; this.disabled=true;" + script;
	}

	@Override
	public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
        return "this.disabled=false;" + script;
	}

	@Override
	public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
		return "this.disabled=false;" + script;
	}
}
