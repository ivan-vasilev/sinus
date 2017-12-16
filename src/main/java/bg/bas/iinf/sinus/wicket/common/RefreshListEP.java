package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Universalen event payload za obnovqvane na spisyci
 * @author hok
 *
 */
public class RefreshListEP {

	private AjaxRequestTarget target;

	public RefreshListEP(AjaxRequestTarget target) {
		super();
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}
}
